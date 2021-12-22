package com.ferechamitbeyli.presentation.utils.helpers

import android.graphics.Color
import com.google.android.gms.maps.model.JointType

object PresentationConstants {

    // Pending Intent
    const val ACTION_START_OR_RESUME_SERVICE = "action_start_or_resume_service"
    const val ACTION_PAUSE_SERVICE = "action_pause_service"
    const val ACTION_STOP_SERVICE = "action_stop_service"
    const val ACTION_NAVIGATE_TO_TRACKING_FRAGMENT = "action_navigate_to_tracking_fragment"

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "keeprun_channel_id"
    const val NOTIFICATION_CHANNEL_NAME = "keeprun_channel"
    const val NOTIFICATION_ID = 5

    // Push Notification
    const val PUSH_NOTIFICATION_CHANNEL_ID = "keeprun_push_channel_id"
    const val PUSH_NOTIFICATION_CHANNEL_NAME = "keeprun_push_channel"
    const val PUSH_NOTIFICATION_ID = 6

    const val PENDING_INTENT_REQUEST_CODE = 4
    const val PENDING_INTENT_PAUSE_REQUEST_CODE = 7
    const val PENDING_INTENT_START_OR_RESUME_REQUEST_CODE = 8

    // Google Maps
    const val LOCATION_UPDATE_INTERVAL = 4000L
    const val LOCATION_FASTEST_UPDATE_INTERVAL = 2000L

    const val FOLLOW_POLYLINE_ZOOM = 18f
    const val TIMER_UPDATE_INTERVAL = 50L

    const val POLYLINE_COLOR = Color.GREEN
    const val POLYLINE_JOINT_TYPE = JointType.ROUND
    const val POLYLINE_WIDTH = 10f

}