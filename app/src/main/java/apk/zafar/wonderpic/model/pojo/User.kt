package apk.zafar.wonderpic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UserResponse(
    val response: List<UserApi>?
)

data class UserApi(
    @SerializedName("first_name")
    val firstName: String?,
    val id: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_max_orig")
    val photo: String
)

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    var id: Int?,
    val name: String?,
    var email: String?,
    val photo: String?
)
