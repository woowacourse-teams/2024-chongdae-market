package com.zzang.chongdae.domain.paging

import android.util.Log
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
            val offerings =
                offeringsRepository.fetchOfferings(
                    filter = filter,
                    search = search,
                    lastOfferingId = lastOfferingId,
                    pageSize = params.loadSize,
                ).getOrThrow()

            val prevKey = if (lastOfferingId == null) null else lastOfferingId + DEFAULT_PAGE_SIZE
            val nextKey =
                if (offerings.isEmpty() || offerings.size < DEFAULT_PAGE_SIZE) null else offerings.last().id

            Log.e("seogi", "prevKey: $prevKey nextKey: $nextKey")

            LoadResult.Page(
                data = offerings,
                prevKey = prevKey,
                nextKey = nextKey,
            )
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
