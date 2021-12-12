package com.ferechamitbeyli.presentation.utils.helpers

import android.util.Log
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

typealias Polyline = MutableList<LatLng>
typealias PolylineList = MutableList<Polyline>

object TrackingHelperFunctions {

    /*
    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if (!includeMillis) {
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds:" +
                "${if (milliseconds < 10) "0" else ""}$milliseconds"
    }
     */

    fun calculateElapsedTime(elapsedTime: Long): String {

        val millis = (elapsedTime % 100)
        val seconds = (elapsedTime / 1000).toInt() % 60
        val minutes = (elapsedTime / (1000 * 60) % 60)
        val hours = (elapsedTime / (1000 * 60 * 60) % 24)

        return "$hours:$minutes:$seconds:$millis"
    }

    fun calculateDistance(locationList: PolylineList, inKilometers: Boolean): Double {
        var meters = 0.0
        return if (locationList.isNotEmpty()) {
            locationList.forEach {
                meters += SphericalUtil.computeDistanceBetween(it.first(), it.last())
            }
            Log.d("CALC_DISTANCE", "inKilometers: $inKilometers, meters: $meters, km: ${meters / 1000}")
            when (inKilometers) {
                true -> meters / 1000
                false -> meters
            }
        } else {
            meters
        }
    }

    /*
    fun calculateDistance(locationList: MutableList<LatLng>, inKilometers: Boolean): Double {
        if (locationList.isNotEmpty()) {
            val meters =
                SphericalUtil.computeDistanceBetween(locationList.first(), locationList.last())
            return if (inKilometers) {
                meters / 1000 // in kilometer
            } else {
                meters // in meter
            }

        }
        return 0.0
    }
     */

    fun calculateMETValue(mph: Double): Float =
        when (mph) {
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