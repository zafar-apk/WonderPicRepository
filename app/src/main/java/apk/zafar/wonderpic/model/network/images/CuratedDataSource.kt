package apk.zafar.wonderpic.model.network.images

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import apk.zafar.wonderpic.model.pojo.Photo
import apk.zafar.wonderpic.model.network.Result
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class CuratedDataSource(
    private val context: Context,
) : PagingSource<Int, Photo>() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DataSourceEntryPoint {
        fun repository(): ImagesRepository
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                    context.applicationContext, DataSourceEntryPoint::class.java
                )
            val repository = hiltEntryPoint.repository()

            val nextPageNumber = params.key ?: 1


            when (val response = repository.getCurated(page = nextPageNumber)) {
                is Result.Success -> {
                    LoadResult.Page(
                        data = response.data.photos ?: emptyList(),
                        prevKey =
                        if (response.data.page ?: 0 > 1) response.data.page?.minus(1) else null,
                        nextKey = nextPageNumber + 1
                    )
                }
                is Result.NetworkError -> {
                    LoadResult.Error(response.exception)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}