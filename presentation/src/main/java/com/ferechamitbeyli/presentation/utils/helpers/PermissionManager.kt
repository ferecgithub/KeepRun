package com.ferechamitbeyli.presentation.utils.helpers

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions

object PermissionManager {

    private const val PERMISSION_LOCATION_REQ_CODE = 1
    private const val PERMISSION_ACTIVITY_RECOGNITION_REQ_CODE = 2

    /** Fine Location Permission functions **/
    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    fun requestLocationPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "This application cannot work without Location Permission",
            PERMISSION_LOCATION_REQ_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    /** Activity Recognition Permission functions **/
    @RequiresApi(Build.VERSION_CODES.Q)
    fun hasActivityRecognitionPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        )

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestActivityRecognitionPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "This application cannot work without Activity Recognition Permission",
            PERMISSION_ACTIVITY_RECOGNITION_REQ_CODE,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    }


}