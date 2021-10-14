package com.ferechamitbeyli.domain.entity

data class Run(
    var img: String? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var caloriesBurned: Int = 0,
    var steps: Int = 0,
    var id: Long = 0L
) {

}