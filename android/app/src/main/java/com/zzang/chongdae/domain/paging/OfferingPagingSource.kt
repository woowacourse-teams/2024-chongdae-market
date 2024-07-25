package com.zzang.chongdae.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zzang.chongdae.domain.model.Offering

class OfferingPagingSource(
    private val fetchOfferings: suspend (lastOfferingId: Long, pageSize: Int) -> List<Offering>,
) : PagingSource<Long, Offering>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Offering> {
        val lastOfferingId = params.key ?: INIT_LAST_OFFERING_ID
        return runCatching {
            val offerings = fetchOfferings(lastOfferingId, params.loadSize)
            LoadResult.Page(
                data = offerings,
                prevKey = if (lastOfferingId == INIT_LAST_OFFERING_ID) null else lastOfferingId - params.loadSize,
                nextKey = if (offerings.isEmpty()) null else lastOfferingId + params.loadSize,
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
        private const val INIT_LAST_OFFERING_ID = 0L
    }
}
