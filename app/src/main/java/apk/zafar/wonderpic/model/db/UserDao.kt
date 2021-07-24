package apk.zafar.wonderpic.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import apk.zafar.wonderpic.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    suspend fun getUser(): User?

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    fun getUserLiveData(): LiveData<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

}
