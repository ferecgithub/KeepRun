package com.ferechamitbeyli.data.remote.entities

import com.ferechamitbeyli.domain.entity.User

data class UserDto(
    var uid: String? = "",
    var email: String? = "",
    var username: String? = "",
    var photoUrl: String? = ""
) {
    companion object {
        fun fromUser(user: User)
                = UserDto(
            user.uid,
            user.email,
            user.username,
            user.photoUrl
        )
    }

    fun toUser()
            = User(uid, email, username, photoUrl)
}