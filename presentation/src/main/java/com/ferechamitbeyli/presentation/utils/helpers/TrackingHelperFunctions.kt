package com.ferechamitbeyli.presentation.utils.helpers

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat
import java.text.NumberFormat

typealias SinglePolyline = MutableList<LatLng>
typealias PolylineList = MutableList<SinglePolyline>

object TrackingHelperFunctions {

    fun calculateElapsedTime(elapsedTime: Long): String {
        val millis = (elapsedTime % 100)
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60) % 60)
        val hours = (elapsedTime / (1000 * 60 * 60) % 24)

        val decimal: NumberFormat = DecimalFormat("00")
        return "${decimal.format(hours)}:${decimal.format(minutes)}:${decimal.format(seconds)}:${
            decimal.format(
                millis
            )
        }"
    }

    fun calculateTimeFromDatabase(elapsedTime: Long): String {
        val millis = (elapsedTime % 100)
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60) % 60)
        val hours = (elapsedTime / (1000 * 60 * 60) % 24)

        val decimal: NumberFormat = DecimalFormat("00")
        val hourDecimal: NumberFormat = DecimalFormat("000")
        return if (hours < 100L) {
            "${decimal.format(hours)}:${decimal.format(minutes)}:${decimal.format(seconds)}:${
                decimal.format(
                    millis
                )
            }"
        } else {
            "${hourDecimal.format(hours)}:${decimal.format(minutes)}:${decimal.format(seconds)}:${
                decimal.format(
                    millis
                )
            }"
        }
    }

    fun calculateDistance(locationList: PolylineList, inKilometers: Boolean): Double {
        var meters = 0.0
        return if (locationList.isNotEmpty()) {
            locationList.forEach {
                meters += SphericalUtil.computeDistanceBetween(it.first(), it.last())
            }
            when (inKilometers) {
                true -> meters / 1000
                false -> meters
            }
        } else {
            meters
        }
    }

    fun calculateMETValue(milesPerHour: Double): Float =
        when (milesPerHour) {
            in 0.0..2.1 -> 2.0f // strolling, very slow
            in 2.1..3.6 -> 4.5f // briskly & carrying objects less than 25 lbs
            in 3.6..5.1 -> 8.0f // running, around 5 mph (12 min/mile)
            in 5.1..7.1 -> 11.5f // running, around 7 mph (8.5 min/mile)
            in 7.1..10.1 -> 16.0f // running, around 10 mph (6 min/mile)
            else -> 20.0f // Hussain Bolt
        }

    fun setCameraPosition(location: LatLng, zoomValue: Float): CameraPosition {
        return CameraPosition.Builder()
            .target(location)
            .zoom(zoomValue) //18f
            .build()
    }

}