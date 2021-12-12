package com.ferechamitbeyli.presentation.utils.helpers

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions

object PermissionManager {

    private const val PERMISSION_LOCATION_REQ_CODE = 1
    private const val PERMISSION_ACTIVITY_RECOGNITION_REQ_CODE = 2
    private const val PERMISSION_BACKGROUND_LOCATION_REQ_CODE = 3

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
    fun hasActivityRecognitionPermission(context: Context) : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        }
        return true
    }

    fun requestActivityRecognitionPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                fragment,
                "This application cannot work without Activity Recognition Permission",
                PERMISSION_ACTIVITY_RECOGNITION_REQ_CODE,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        }
    }

    /** Background Location Permission functions **/
    fun hasBackgroundLocationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        return true
    }

    fun requestBackgroundLocationPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                fragment,
                "This application cannot work without Background Location Permission",
                PERMISSION_BACKGROUND_LOCATION_REQ_CODE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }


}