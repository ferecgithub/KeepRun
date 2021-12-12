package com.ferechamitbeyli.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.PUSH_NOTIFICATION_CHANNEL_ID
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.PUSH_NOTIFICATION_CHANNEL_NAME
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.PUSH_NOTIFICATION_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebasePushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val text = remoteMessage.notification?.body

        createPushNotificationChannel()
        val notification = NotificationCompat.Builder(this, PUSH_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_run_notification)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(PUSH_NOTIFICATION_ID, notification.build())

        super.onMessageReceived(remoteMessage)
    }

    private fun createPushNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PUSH_NOTIFICATION_CHANNEL_ID,
                PUSH_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}