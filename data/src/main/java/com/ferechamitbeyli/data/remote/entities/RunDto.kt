package com.ferechamitbeyli.data.remote.entities

data class RunDto(
    val imageUrl: String? = null,
    val timestamp: Long = 0L,
    val avgSpeedInKMH: Double = 0.0,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val caloriesBurned: Int = 0,
    val steps: Int = 0,
    val id: Long = 0L
) {
}