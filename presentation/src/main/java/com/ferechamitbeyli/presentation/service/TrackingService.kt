package com.ferechamitbeyli.presentation.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.utils.helpers.PolylineList
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_PAUSE_SERVICE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_START_OR_RESUME_SERVICE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_STOP_SERVICE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.LOCATION_FASTEST_UPDATE_INTERVAL
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.LOCATION_UPDATE_INTERVAL
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.NOTIFICATION_CHANNEL_ID
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.NOTIFICATION_CHANNEL_NAME
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.NOTIFICATION_ID
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.PENDING_INTENT_PAUSE_REQUEST_CODE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.PENDING_INTENT_START_OR_RESUME_REQUEST_CODE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.TIMER_UPDATE_INTERVAL
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.calculateDistance
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.setPendingIntentFlag
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : LifecycleService(), SensorEventListener {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var sensorManager: SensorManager

    private val localBinder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getTrackingServiceInstance(): TrackingService = this@TrackingService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return localBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    private val _isFirstRun = MutableLiveData<Boolean>(true)
    val isFirstRun: LiveData<Boolean> get() = _isFirstRun

    private var neglectedStepCount = -1

    private val _isKilled = MutableLiveData<Boolean>(false)
    val isKilled: LiveData<Boolean> get() = _isKilled

    private val _started = MutableLiveData<Boolean>(false)
    val started: LiveData<Boolean> get() = _started

    private val _locationList = MutableLiveData<PolylineList>()
    val locationList: LiveData<PolylineList> get() = _locationList

//    private val _startTime = MutableLiveData<Long>()

    private val _totalTime = MutableLiveData<Long>()
    val totalTime: LiveData<Long> get() = _totalTime

    private val _stepCount = MutableLiveData<Int>()
    val stepCount: LiveData<Int> get() = _stepCount


    private fun setInitialValues() = lifecycleScope.launch {
        neglectedStepCount = -1
        isTimerEnabled = false
        _isFirstRun.postValue(true)
        _started.postValue(false)
        _isKilled.postValue(false)
//        _startTime.postValue(0L)
        _stepCount.postValue(-1)
        _totalTime.postValue(0L)

        _locationList.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder = notificationBuilder

        setInitialValues()

        setupStepCounterListener()

        started.observe(this) {
            startLocationUpdates(it)
            updateNotificationState(it)
        }
    }


    private fun setupStepCounterListener() {
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent ?: return
        sensorEvent.values.firstOrNull()?.let {

            if (neglectedStepCount == -1 || _started.value == false) {
                neglectedStepCount = it.toInt()
            } else {
                _stepCount.postValue(it.toInt() - neglectedStepCount)
                Log.d("SERVICE_TAG_STEPS", "Step count: ${_stepCount.value}")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("SERVICE_TAG", "onAccuracyChanged Sensor: $sensor, accuracy: $accuracy")
    }

    private fun addEmptyPolyline() = _locationList.value?.apply {
        add(mutableListOf())
        _locationList.postValue(this)
    } ?: _locationList.postValue(mutableListOf(mutableListOf()))


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun.value == true) {
                        _isFirstRun.postValue(false)
                        startForegroundService()
//                        _startTime.postValue(System.currentTimeMillis())
                    } else {
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseForegroundService()
                }
                ACTION_STOP_SERVICE -> {
                    stopForegroundService()
                }
                else -> {
                    /** NO-OP **/
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun startForegroundService() {
        _isKilled.postValue(false)
        createNotificationChannel()
        startForeground(
            NOTIFICATION_ID,
            notificationBuilder.build()
        )
        startTimer()
    }

    private fun pauseForegroundService() {
        _started.postValue(false)
        isTimerEnabled = false
    }

    private fun stopForegroundService() {
        _isKilled.postValue(true)
        _isFirstRun.postValue(true)
        pauseForegroundService()
        setInitialValues()
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(isStarted: Boolean) {
        when (isStarted) {
            true -> {
                val locationRequest = LocationRequest.create().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
            false -> removeLocationUpdates()
        }
    }

    private fun updateNotificationState(isStarted: Boolean) {
        val actionText = if (isStarted) "Pause" else "Resume"
        val pendingIntent = if (isStarted) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(
                this,
                PENDING_INTENT_PAUSE_REQUEST_CODE,
                pauseIntent,
                setPendingIntentFlag()
            )
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(
                this,
                PENDING_INTENT_START_OR_RESUME_REQUEST_CODE,
                resumeIntent,
                setPendingIntentFlag()
            )
        }

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if (isKilled.value == false) {
            curNotificationBuilder = notificationBuilder.apply {
                addAction(R.drawable.ic_pause, actionText, pendingIntent)
            }
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (_started.value == true) {
                result.locations.let { locations ->
                    locations.forEach {
                        updateLocationList(it)
                        updateNotificationPeriodically()
                    }
                }
            }

        }
    }

    private fun updateNotificationPeriodically() = lifecycleScope.launch {
        notificationBuilder.apply {
            setContentTitle("KeepRun | You've run:")
            setContentText(
                "${
                    _locationList.value?.let {
                        DecimalFormat("#.##").format(
                            calculateDistance(it, true)
                        )
                    }
                }km"
            )
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocationList(location: Location?) = lifecycleScope.launch {
        location?.let {
            val newLatLng = LatLng(location.latitude, location.longitude)

            _locationList.value?.apply {
                last().add(newLatLng)
                _locationList.postValue(this)
            }
        }
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var runTime = 0L
    private var startTime = 0L

    private fun startTimer() {
        addEmptyPolyline()
        _started.postValue(true)
        startTime = System.currentTimeMillis()
        isTimerEnabled = true
        lifecycleScope.launch {
            while (started.value!! && isTimerEnabled) {
                lapTime = System.currentTimeMillis() - startTime
                _totalTime.postValue(runTime + lapTime)
            }
            runTime += lapTime
            delay(TIMER_UPDATE_INTERVAL)
        }

        //Log.d("SERVICE_TIME", "totalTime: ${totalTime.value}, startTime: ${_startTime.value!!}, runTime: $runTime")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.coroutineContext.cancelChildren()
    }

}