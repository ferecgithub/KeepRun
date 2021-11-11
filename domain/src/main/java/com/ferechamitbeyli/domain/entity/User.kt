package com.ferechamitbeyli.domain.entity

data class User(
    var uid: String? = "",
    var email: String? = "",
    var username: String? = "",
    var weight: Double = 0.0,
    var isNotificationEnable: Boolean = true,
    var photoUrl: String? = ""
)