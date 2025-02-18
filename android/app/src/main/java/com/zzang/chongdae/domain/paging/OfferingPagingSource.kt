package com.zzang.chongdae.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.usecase.home.FetchOfferingsUseCase

class OfferingPagingSource(
    private val fetchOfferingsUseCase: FetchOfferingsUseCase,
    private val authRepository: AuthRepository,
    private val search: String?,
    private val filter: String?,
    private val retry: () -> Unit,
) : PagingSource<Long, Offering>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Offering> {
        val lastOfferingId = params.key
        return runCatching {
            val offerings =
                fetchOfferingsUseCase(
                    filter = filter,
                    search = search,
                    lastOfferingId = lastOfferingId,
                    pageSize = params.loadSize,
                )

            when (offerings) {
                is Result.Error -> {
                    when (offerings.error) {
                        DataError.Network.UNAUTHORIZED -> {
                            authRepository.saveRefresh()
                            retry()
                            load(params)
                        }

                        else -> {
                            val prevKey: Long? = null
                            val nextKey: Long? = null
                            LoadResult.Page(
                                data = emptyList<Offering>(),
                                prevKey = prevKey,
                                nextKey = nextKey,
                            )
                        }
                    }
                }

                is Result.Success -> {
                    val prevKey =
                        if (lastOfferingId == null) null else lastOfferingId + DEFAULT_PAGE_SIZE
                    val nextKey =
                        if (offerings.data.isEmpty() || offerings.data.size < DEFAULT_PAGE_SIZE) null else offerings.data.last().id

                    LoadResult.Page(
                        data = offerings.data,
                        prevKey = prevKey,
                        nextKey = nextKey,
                    )
                }
            }
        }.onFailure { throwable ->
            LoadResult.Error<Long, Offering>(throwable)
        }.getOrThrow()
    }

    override fun getRefreshKey(state: PagingState<Long, Offering>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.minus(DEFAULT_PAGE_SIZE)
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }
}
