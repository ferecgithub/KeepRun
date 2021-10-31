package com.ferechamitbeyli.data.remote.entities

data class UserDto(
    var uid: String? = "",
    var email: String? = "",
    var username: String? = "",
    var isNotificationEnable: Boolean = true,
    var photoUrl: String? = ""
)