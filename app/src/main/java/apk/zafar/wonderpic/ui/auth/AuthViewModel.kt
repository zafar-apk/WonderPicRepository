package apk.zafar.wonderpic.ui.auth

import android.app.Activity
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apk.zafar.wonderpic.KEY_AUTHENTICATED
import apk.zafar.wonderpic.model.User
import apk.zafar.wonderpic.model.db.UserDatabase
import apk.zafar.wonderpic.model.network.Result
import apk.zafar.wonderpic.model.network.vk.VKRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: VKRepository,
    private val userDb: UserDatabase,
    private val preferences: SharedPreferences
) : ViewModel() {

    val isAuthorized: Boolean
        get() = preferences.getBoolean(KEY_AUTHENTICATED, false)

    private val error = MutableLiveData<Exception?>()

    fun loginVK(activity: Activity) {
        VK.login(
            activity,
            arrayListOf(
                VKScope.WALL,
                VKScope.PHOTOS,
                VKScope.EMAIL
            )
        )
    }

    fun saveToken(token: VKAccessToken, action: () -> Unit) {
        viewModelScope.launch {
            when (val result = repository.saveToken(token)) {
                is Result.Success -> {
                    result.data.response?.lastOrNull()?.let { userApi ->
                        with(userApi) {
                            val newUser = User(
                                id = id,
                                email = token.email,
                                photo = photo,
                                name = "$firstName $lastName"
                            )
                            insertToDb(newUser)
                            preferences.edit {
                                putBoolean(KEY_AUTHENTICATED, true)
                            }
                            action()
                        }
                    }
                }
                is Result.NetworkError -> error.value = result.exception
            }
        }
    }

    private suspend fun insertToDb(user: User) {
        withContext(Dispatchers.IO) {
            userDb.userDao().insert(user)
        }
    }

}
