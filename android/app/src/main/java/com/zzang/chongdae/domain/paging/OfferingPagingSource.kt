package com.zzang.chongdae.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.repository.OfferingRepository

class OfferingPagingSource(
    private val offeringsRepository: OfferingRepository,
    private val search: String?,
    private val filter: String?,
) : PagingSource<Long, Offering>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Offering> {
        val lastOfferingId = params.key
        return runCatching {
            val offerings = offeringsRepository.fetchOfferings(
                filter = filter,
                search = search,
                lastOfferingId = lastOfferingId,
                pageSize = params.loadSize
            )

            LoadResult.Page(
                data = offerings,
                prevKey = lastOfferingId,
                nextKey = if (offerings.isEmpty()) null else offerings.last().id,
            )
        }.onFailure { throwable ->
            LoadResult.Error<Long, Offering>(throwable)
        }.getOrThrow()
    }

    override fun getRefreshKey(state: PagingState<Long, Offering>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 0L
    }
}
