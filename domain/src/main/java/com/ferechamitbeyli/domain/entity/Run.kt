package com.ferechamitbeyli.domain.entity

data class Run(
    val imageUrl: String? = null,
    val image: ByteArray? = null,
    val timestamp: Long = 0L,
    val avgSpeedInKMH: Double = 0.0,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val caloriesBurned: Int = 0,
    val steps: Int = 0,
    val id: Long = 0L
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Run

        if (imageUrl != other.imageUrl) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (timestamp != other.timestamp) return false
        if (avgSpeedInKMH != other.avgSpeedInKMH) return false
        if (distanceInMeters != other.distanceInMeters) return false
        if (timeInMillis != other.timeInMillis) return false
        if (caloriesBurned != other.caloriesBurned) return false
        if (steps != other.steps) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageUrl?.hashCode() ?: 0
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + avgSpeedInKMH.hashCode()
        result = 31 * result + distanceInMeters
        result = 31 * result + timeInMillis.hashCode()
        result = 31 * result + caloriesBurned
        result = 31 * result + steps
        result = 31 * result + id.hashCode()
        return result
    }
}