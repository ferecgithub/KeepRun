package com.ferechamitbeyli.domain.entity

data class User(
    val email: String? = "",
    val username: String? = "",
    val weight: Double = 0.0,
    val isNotificationEnable: Boolean = true,
    val photoUrl: String? = ""
)