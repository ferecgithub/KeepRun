package com.ferechamitbeyli.keeprun.utils

import android.graphics.Color

object Constants {

    // Room DB
    const val KEEPRUN_DB_NAME = "keeprun_db"

    // DataStore Preferences
    const val KEEPRUN_CACHE_NAME = "keeprun_cache"

    const val CACHE_KEY_FOR_USERNAME = "user_name"
    const val CACHE_KEY_FOR_WEIGHT = "user_weight"
    const val CACHE_KEY_FOR_FIRST_TIME_USE = "is_first_time"
    const val CACHE_KEY_FOR_HAS_PERMISSION = "has_permission"


    // Pending Intent
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "keeprun_channel"
    const val NOTIFICATION_CHANNEL_NAME = "keeprun"
    const val NOTIFICATION_ID = 1

    // Cancel Dialog Tag
    const val CANCEL_TRACKING_DIALOG_TAG = "CANCEL_TRACKING_DIALOG_TAG"

    // Google Maps
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val TIMER_UPDATE_INTERVAL = 50L

    const val POLYLINE_COLOR = Color.GREEN
    const val POLYLINE_WIDTH = 8f

    const val MAP_CAMERA_ZOOM = 15f






}