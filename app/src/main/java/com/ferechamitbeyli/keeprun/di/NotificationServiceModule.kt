package com.ferechamitbeyli.keeprun.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import androidx.core.app.NotificationCompat
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_NAVIGATE_TO_TRACKING_FRAGMENT
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.NOTIFICATION_CHANNEL_ID
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.PENDING_INTENT_REQUEST_CODE
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.setPendingIntentFlag
import com.ferechamitbeyli.presentation.view.activities.home_activity.HomeActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
class NotificationServiceModule {

    @Provides
    @ServiceScoped
    fun provideNotificationBuilder(
        @ApplicationContext appContext: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(appContext, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_run_notification)
        .setContentIntent(pendingIntent)

    @Provides
    @ServiceScoped
    fun providePendingIntent(
        @ApplicationContext appContext: Context,
    ): PendingIntent = PendingIntent.getActivity(
        appContext,
        PENDING_INTENT_REQUEST_CODE,
        Intent(appContext, HomeActivity::class.java).apply {
            this.action = ACTION_NAVIGATE_TO_TRACKING_FRAGMENT
        },
        setPendingIntentFlag()
    )

    @Provides
    @ServiceScoped
    fun provideNotificationManager(
        @ApplicationContext appContext: Context
    ): NotificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @ServiceScoped
    fun provideSensorManager(
        @ApplicationContext appContext: Context
    ): SensorManager = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

}