package apk.zafar.wonderpic.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import apk.zafar.wonderpic.KEY_AUTHENTICATED
import apk.zafar.wonderpic.model.db.UserDatabase
import apk.zafar.wonderpic.model.network.images.CuratedDataSource
import apk.zafar.wonderpic.model.network.images.SearchDataSource
import apk.zafar.wonderpic.model.pojo.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: UserDatabase,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val pagingConfig = PagingConfig(
        pageSize = 30,
        enablePlaceholders = false
    )

    fun search(query: String, context: Context): Flow<PagingData<Photo>> {
        return Pager(
            pagingConfig,
            pagingSourceFactory = {
                SearchDataSource(
                    query = query,
                    context = context
                )
            }
        ).flow
            .cachedIn(viewModelScope)
    }

    fun fetchCurated(context: Context): Flow<PagingData<Photo>> {
        return Pager(
            pagingConfig,
            pagingSourceFactory = {
                CuratedDataSource(context = context)
            }
        ).flow
            .cachedIn(viewModelScope)
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        db.userDao().getUserLiveData()
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        preferences.edit {
            putBoolean(KEY_AUTHENTICATED, false)
        }
        db.clearAllTables()
    }

}