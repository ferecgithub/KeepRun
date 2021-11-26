package com.ferechamitbeyli.data.utils

import android.graphics.Color

object Constants {

    // Room DB
    const val KEEPRUN_DB_NAME = "keeprun_db"

    // DataStore Preferences
    const val KEEPRUN_CACHE_NAME = "keeprun_cache"

    const val CACHE_KEY_FOR_USER_UID = "user_uid"
    const val CACHE_KEY_FOR_USERNAME = "user_name"
    const val CACHE_KEY_FOR_USER_EMAIL = "user_email"
    const val CACHE_KEY_FOR_USER_WEIGHT = "user_weight"
    const val CACHE_KEY_FOR_NOTIFICATION_ENABLED = "notification_enabled"
    const val CACHE_KEY_FOR_USER_PHOTO_URL = "user_photo_url"
    const val CACHE_KEY_FOR_FIRST_TIME_USE = "is_first_use"
    const val CACHE_KEY_FOR_HAS_FINE_LOCATION_PERMISSION = "has_fine_location_permission"
    const val CACHE_KEY_FOR_HAS_ACTIVITY_RECOGNITION_PERMISSION = "has_activity_recognition_permission"


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

    // Firebase Realtime Database
    const val FIREBASE_DB_REF = "https://keeprun-c873e-default-rtdb.europe-west1.firebasedatabase.app"
    const val USERS_TABLE_REF = "Users"

    const val USERS_TABLE_UID_REF = "uid"
    const val USERS_TABLE_USERNAME_REF = "username"
    const val USERS_TABLE_EMAIL_REF = "email"
    const val USERS_TABLE_WEIGHT_REF = "weight"
    const val USERS_TABLE_NOTIFICATION_ENABLE_REF = "notificationEnable"
    const val USERS_TABLE_PHOTO_URL_REF = "photoUrl"

    const val RUNS_TABLE_REF = "Runs"

    const val RUNS_TABLE_IMAGE_URL_REF = "imageUrl"
    const val RUNS_TABLE_TIMESTAMP_REF = "timestamp"
    const val RUNS_TABLE_AVG_SPEED_KMH_REF = "avgSpeedInKMH"
    const val RUNS_TABLE_DISTANCE_METERS_REF = "distanceInMeters"
    const val RUNS_TABLE_TIME_MILLIS_REF = "timeInMillis"
    const val RUNS_TABLE_CALORIES_BURNED_REF = "caloriesBurned"
    const val RUNS_TABLE_STEPS_REF = "steps"

    // Firebase Storage
    const val FIREBASE_STORAGE_REF = "gs://keeprun-c873e.appspot.com"
    const val USERS_STORAGE_REF = "Users"
    const val RUNS_STORAGE_REF = "Runs"




}