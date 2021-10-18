package com.ferechamitbeyli.keeprun.framework.model.remote.enitities

import com.ferechamitbeyli.domain.entity.User

data class UserEntity(
    var email: String = "",
    var username: String = ""
) {
    companion object {
        fun fromUser(user: User)
                = UserEntity(
            user.email,
            user.username
        )
    }

    fun toUser()
            = User(email, username)
}