package apk.zafar.wonderpic.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import apk.zafar.wonderpic.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}