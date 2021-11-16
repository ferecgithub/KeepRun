package com.ferechamitbeyli.data.remote.entities

data class UserDto(
    val email: String? = "",
    val username: String? = "",
    val weight: Double = 0.0,
    val isNotificationEnable: Boolean = true,
    val photoUrl: String? = ""
)