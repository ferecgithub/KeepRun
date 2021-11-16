package com.ferechamitbeyli.presentation.uimodels

import android.graphics.Bitmap

data class RunUIModel(
    val imageUrl: String? = null,
    val image: Bitmap? = null,
    val timestamp: Long = 0L,
    val avgSpeedInKMH: Double = 0.0,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val caloriesBurned: Int = 0,
    val steps: Int = 0,
    val id: Long = 0L,
    var isExpanded : Boolean = false
) {
}