package com.ferechamitbeyli.presentation.uimodels

data class UserUIModel(
    val email: String? = "",
    val username: String? = "",
    val weight: Double = 0.0,
    val isNotificationEnable: Boolean = true,
    val photoUrl: String? = ""
)
