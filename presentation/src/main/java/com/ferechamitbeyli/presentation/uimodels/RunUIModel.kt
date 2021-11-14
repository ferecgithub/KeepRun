package com.ferechamitbeyli.presentation.uimodels

import android.graphics.Bitmap

data class RunUIModel(
    var imageUrl: String? = null,
    var image: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Double = 0.0,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0,
    var steps: Int = 0,
    var isExpanded : Boolean = false,
    var id: Long = 0L
) {
}