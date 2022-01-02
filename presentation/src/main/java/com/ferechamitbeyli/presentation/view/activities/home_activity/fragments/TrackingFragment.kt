package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.CancelRunDialogBinding
import com.ferechamitbeyli.presentation.databinding.FragmentTrackingBinding
import com.ferechamitbeyli.presentation.service.TrackingService
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.utils.enums.RunButtonsState
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_PAUSE_SERVICE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_START_OR_RESUME_SERVICE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.ACTION_STOP_SERVICE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.FOLLOW_POLYLINE_ZOOM
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.POLYLINE_COLOR
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.POLYLINE_JOINT_TYPE
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants.POLYLINE_WIDTH
import com.ferechamitbeyli.presentation.utils.helpers.SinglePolyline
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.calculateDistance
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.calculateElapsedTime
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.calculateMETValue
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.fromVectorToBitmap
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.showSnackbar
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.visible
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.TrackingViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : BaseFragment<FragmentTrackingBinding>(), LocationListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap

    private val viewModel: TrackingViewModel by viewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var serviceIntent: Intent

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    private val isFirstRun = MutableLiveData<Boolean>()
    private val isStarted = MutableLiveData<Boolean>()
    private val isKilled = MutableLiveData<Boolean>()

    private var runTimeInMillis = 0L
    private var weight = 0.0

    private var locationList = mutableListOf<SinglePolyline>()
    private var polylineList = mutableListOf<Polyline>()

    private var markerList = mutableListOf<Marker>()

    private var stepCount = 0

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTrackingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternetConnection()

        setupGoogleMap(savedInstanceState)

        initiateServiceIntent()

        setupBottomNavigationViewVisibility()

        setupOnClickListeners()

        getWeightFromCache()

        listenEventChannel()
    }

    private fun initiateServiceIntent() {
        serviceIntent = Intent(requireContext(), TrackingService::class.java)
        checkForService()
    }

    private fun getWeightFromCache() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.getUserWeightFromCache().collect {
            weight = it
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupGoogleMap(savedInstanceState: Bundle?) {

        binding.trackingMapMv.onCreate(savedInstanceState)

        binding.trackingMapMv.getMapAsync { googleMap ->
            map = googleMap
            map.isMyLocationEnabled = true
            if (currentLatitude != null && currentLongitude != null) {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(currentLatitude!!, currentLongitude!!),
                        15f
                    )
                )
            }
            map.uiSettings.apply {
                setAllGesturesEnabled(false)
                isCompassEnabled = false
            }
        }
    }

    private fun checkForService() {
        if (isKilled.value == false || isKilled.value == null) {
            requireActivity().bindService(
                serviceIntent,
                getServiceConnection(),
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun setupOnClickListeners() {
        binding.startRunBtn.setOnClickListener {
            checkPermissionsBeforeStartingTheRun()
        }

        binding.pauseRunBtn.setOnClickListener {
            pauseTheRun()
        }

        binding.finishRunBtn.setOnClickListener {
            showCancelRunDialog()
        }

        binding.goBackBtn.setOnClickListener {
            resetMap()
            navigateToRunsFragment()
        }

        binding.resetRunBtn.setOnClickListener {
            resetMap()
            setupRunButtons(RunButtonsState.INITIAL)
        }
    }

    private fun setupRunButtonsForServiceState() {
        if (isKilled.value == false) {
            if (isFirstRun.value == true) {
                setupRunButtons(RunButtonsState.INITIAL)
            } else {
                if (isStarted.value == true) {
                    setupRunButtons(RunButtonsState.STARTED)
                } else {
                    setupRunButtons(RunButtonsState.PAUSED)
                }
            }
        } else {
            setupRunButtons(RunButtonsState.STOPPED)
        }
    }

    private fun setupRunButtons(state: RunButtonsState) {
        when (state) {
            RunButtonsState.INITIAL -> {
                // Visible
                binding.startRunBtn.visible(true)
                binding.startTv.visible(true)
                binding.goBackBtn.visible(true)
                binding.goBackTv.visible(true)

                // Hidden
                binding.pauseRunBtn.visible(false)
                binding.finishRunBtn.visible(false)
                binding.pauseTv.visible(false)
                binding.finishTv.visible(false)
                binding.resumeTv.visible(false)
                binding.resetRunBtn.visible(false)
                binding.resetTv.visible(false)
            }
            RunButtonsState.STARTED -> {
                // Visible
                binding.pauseRunBtn.visible(true)
                binding.finishRunBtn.visible(true)
                binding.pauseTv.visible(true)
                binding.finishTv.visible(true)

                // Hidden
                binding.startRunBtn.visible(false)
                binding.startTv.visible(false)
                binding.goBackBtn.visible(false)
                binding.goBackTv.visible(false)
                binding.resumeTv.visible(false)
                binding.resetRunBtn.visible(false)
                binding.resetTv.visible(false)
            }
            RunButtonsState.PAUSED -> {
                // Visible
                binding.startRunBtn.visible(true)
                binding.resumeTv.visible(true)
                binding.finishRunBtn.visible(true)
                binding.finishTv.visible(true)

                // Hidden
                binding.pauseRunBtn.visible(false)
                binding.pauseTv.visible(false)
                binding.startTv.visible(false)
                binding.goBackBtn.visible(false)
                binding.goBackTv.visible(false)
                binding.resetRunBtn.visible(false)
                binding.resetTv.visible(false)
            }
            RunButtonsState.STOPPED -> {
                // Visible
                binding.resetRunBtn.visible(true)
                binding.resetTv.visible(true)
                binding.goBackBtn.visible(true)
                binding.goBackTv.visible(true)

                // Hidden
                binding.finishRunBtn.visible(false)
                binding.finishTv.visible(false)
                binding.pauseRunBtn.visible(false)
                binding.pauseTv.visible(false)
                binding.startRunBtn.visible(false)
                binding.startTv.visible(false)
                binding.resumeTv.visible(false)
            }
        }
    }

    private fun setInitialValues() {
        locationList = mutableListOf()
        markerList = mutableListOf()
        runTimeInMillis = 0L
        stepCount = 0
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun checkPermissionsBeforeStartingTheRun() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (PermissionManager.hasActivityRecognitionPermission(requireContext()) &&
                PermissionManager.hasBackgroundLocationPermission(requireContext())
            ) {
                checkLocationOptionBeforeStartingTheRun()
            } else {
                navigateToActivityRecognitionPermission()
            }
        } else {
            checkLocationOptionBeforeStartingTheRun()
        }
    }

    private fun checkLocationOptionBeforeStartingTheRun() {
        if (isLocationEnabled(requireContext())) {
            sendActionCommandToService(ACTION_START_OR_RESUME_SERVICE)
        } else {
            showSnackbar(
                binding.root,
                requireContext(),
                false,
                getString(R.string.phone_location_alert),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun pauseTheRun() {
        sendActionCommandToService(ACTION_PAUSE_SERVICE)
    }

    private fun stopTheRun() {
        sendActionCommandToService(ACTION_STOP_SERVICE)
    }

    private fun sendActionCommandToService(action: String) {
        serviceIntent.apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    private fun getServiceConnection() = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            val localBinder = iBinder as TrackingService.LocalBinder
            val service = localBinder.getTrackingServiceInstance()
            viewLifecycleOwner.lifecycleScope.launch {

                service.isKilled.observe(viewLifecycleOwner) { killed ->
                    isKilled.value = killed
                    if (killed) {
                        setupRunButtons(RunButtonsState.STOPPED)
                    }
                }

                service.isFirstRun.observe(viewLifecycleOwner) { firstRun ->
                    isFirstRun.value = firstRun
                }

                service.started.observe(viewLifecycleOwner) { started ->
                    isStarted.value = started
                    setupRunButtonsForServiceState()
                }

                service.locationList.observe(viewLifecycleOwner) { locations ->
                    locationList = locations
                    drawPolylines()
                    followUserWithCamera()
                }


                service.stepCount.observe(viewLifecycleOwner) {
                    stepCount = it
                }

                service.totalTimeInMillis.observe(viewLifecycleOwner) { totalTime ->
                    if (isKilled.value == false) {
                        runTimeInMillis = totalTime
                        binding.trackingTimerTv.text = calculateElapsedTime(totalTime)
                    } else {
                        binding.trackingTimerTv.text = getString(R.string.timer_start_value)
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            /** NO-OP **/
        }
    }

    private fun drawPolylines() {
        if (locationList.isNotEmpty() &&
            isKilled.value == false
        ) {
            locationList.forEach {
                val polyline = map.addPolyline(
                    PolylineOptions().apply {
                        width(POLYLINE_WIDTH)
                        color(POLYLINE_COLOR)
                        jointType(POLYLINE_JOINT_TYPE)
                        startCap(ButtCap())
                        endCap(ButtCap())
                        addAll(it)
                    }
                )
                polylineList.add(polyline)
            }
        }
    }

    private fun followUserWithCamera() {
        if (locationList.isNotEmpty() &&
            locationList.last().isNotEmpty() &&
            isKilled.value == false
        ) {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    locationList.last().last(),
                    FOLLOW_POLYLINE_ZOOM
                )
            )
        }
    }

    private fun showWholePath() {
        if (locationList.isNotEmpty() &&
            isKilled.value == false
        ) {
            val bounds = LatLngBounds.Builder()
            var isBoundsEmpty = true

            locationList.forEach { polyline ->
                polyline.forEach {
                    bounds.include(it)
                    isBoundsEmpty = false
                }
            }
            if (!isBoundsEmpty) {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        100
                    ), 1500, null
                )
                addStartMarker(locationList.first().first())
                addEndMarker(locationList.last().last())
            }
        }
    }

    private fun saveRunToDBWithScreenshot() {
        var distanceInMeters = 0.0
        if (locationList.first().isNotEmpty()) {
            distanceInMeters = calculateDistance(locationList, false)
        }
        if (distanceInMeters >= 10.0) {
            val timeInMs = runTimeInMillis
            val steps = stepCount
            val avgSpeedInKMH =
                round((distanceInMeters / 1000f) / (timeInMs / 1000f / 60 / 60) * 10) / 10f
            val timestamp = Calendar.getInstance().timeInMillis

            // caloriesBurned = MET * 3.5 * weight / 200
            val caloriesBurned =
                (calculateMETValue(convertKilometersPerHourToMilesPerHour(avgSpeedInKMH)) * 3.5 * weight / 200 * timeInMs / 1000f / 60).toInt()
            map.snapshot { screenshot ->
                val run = RunUIModel(
                    image = screenshot,
                    timestamp = timestamp,
                    avgSpeedInKMH = avgSpeedInKMH,
                    distanceInMeters = distanceInMeters.toInt(),
                    timeInMillis = timeInMs,
                    caloriesBurned = caloriesBurned,
                    steps = steps
                )

                viewModel.saveRunToSynced(run)
            }
            stopTheRun()
        } else {
            showSnackbar(
                binding.root,
                requireContext(),
                false,
                getString(R.string.too_low_distance),
                Snackbar.LENGTH_LONG
            ).show()
            stopTheRun()
        }

    }

    private fun convertKilometersPerHourToMilesPerHour(kmsPerHour: Double) = kmsPerHour * 0.621

    private fun addStartMarker(position: LatLng) {
        val marker = map.addMarker(
            MarkerOptions()
                .position(position)
                .icon(
                    fromVectorToBitmap(
                        R.drawable.ic_runs_tab,
                        ContextCompat.getColor(requireContext(), R.color.darkGreen),
                        resources
                    )
                )
        )
        marker?.let { markerList.add(it) }
    }

    private fun addEndMarker(position: LatLng) {
        val marker = map.addMarker(
            MarkerOptions()
                .position(position)
                .icon(
                    fromVectorToBitmap(
                        R.drawable.ic_marker_finished,
                        ContextCompat.getColor(requireContext(), R.color.darkGreen),
                        resources
                    )
                )
        )
        marker?.let { markerList.add(it) }
    }

    @SuppressLint("MissingPermission")
    private fun resetMap() {
        fusedLocationProviderClient.locationAvailability.addOnCompleteListener {
            if (it.result.isLocationAvailable) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { locationResponse ->
                    val lastKnownLocation = LatLng(
                        locationResponse.result.latitude,
                        locationResponse.result.longitude
                    )
                    map.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            TrackingHelperFunctions.setCameraPosition(
                                lastKnownLocation,
                                FOLLOW_POLYLINE_ZOOM
                            )
                        )
                    )
                    polylineList.forEach { polyline -> polyline.remove() }
                    markerList.forEach { marker -> marker.remove() }
                    locationList.clear()
                    markerList.clear()
                    setInitialValues()
                }
            }
        }
    }

    private fun navigateToRunsFragment() {
        if (findNavController().currentDestination?.id == R.id.trackingFragment) {
            findNavController().navigate(R.id.action_trackingFragment_to_runsFragment)
        }
    }

    private fun navigateToActivityRecognitionPermission() {
        if (findNavController().currentDestination?.id == R.id.trackingFragment) {
            findNavController().navigate(R.id.action_trackingFragment_to_activityRecognitionPermissionFragment)
        }
    }

    private fun listenEventChannel() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.trackingEventsChannel.collectLatest {
            when (it) {
                is EventState.Error -> {
                    showSnackbar(
                        binding.root,
                        requireContext(),
                        false,
                        it.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                    setAvailabilityOfFinishedButtonsForUploadingImage(true)
                }
                is EventState.Loading -> {
                    /** NO-OP **/
                    setAvailabilityOfFinishedButtonsForUploadingImage(false)
                }
                is EventState.Success -> {
                    showSnackbar(
                        binding.root,
                        requireContext(),
                        true,
                        it.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    setAvailabilityOfFinishedButtonsForUploadingImage(true)
                }
            }
        }
    }

    private fun showCancelRunDialog() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            val customDialog = Dialog(requireContext())

            customDialog.window?.apply {
                setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                setGravity(Gravity.CENTER)
                setBackgroundDrawable(getBlurBackgroundDrawable())
                attributes?.windowAnimations = android.R.style.Animation_Activity
            }

            customDialog.setCancelable(false)

            val bindingCancelRun: CancelRunDialogBinding =
                CancelRunDialogBinding.inflate(layoutInflater)
            customDialog.setContentView(bindingCancelRun.root)

            bindingCancelRun.cancelRunConfirmBtn.setOnClickListener {
                /** CANCEL THE RUN HERE **/
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    showWholePath()
                    customDialog.dismiss()
                    pauseTheRun()
                    delay(2000)
                    saveRunToDBWithScreenshot()
                }
            }

            bindingCancelRun.cancelRunAbortBtn.setOnClickListener {
                customDialog.dismiss()
            }

            customDialog.show()
        }

    private fun setAvailabilityOfFinishedButtonsForUploadingImage(isEnabled: Boolean) {
        binding.resetRunBtn.enable(isEnabled)
        binding.goBackBtn.enable(isEnabled)
    }

    private fun setStartButtonsForNoInternetConnection(isConnected: Boolean) {
        when (isConnected) {
            true -> {
                binding.resetRunBtn.enable(true)
                binding.startRunBtn.enable(true)
                binding.goBackBtn.enable(true)
            }
            false -> {
                binding.resetRunBtn.enable(false)
                binding.startRunBtn.enable(false)
                binding.goBackBtn.enable(true)
            }
        }
    }

    override fun onResume() {
        binding.trackingMapMv.onResume()
        super.onResume()
    }

    override fun onStart() {
        binding.trackingMapMv.onStart()
        super.onStart()
    }

    override fun onPause() {
        binding.trackingMapMv.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.trackingMapMv.onStart()
        super.onStop()
    }

    override fun onDestroyView() {
        map.clear()
        binding.trackingMapMv.onDestroy()
        binding.trackingMapMv.removeAllViews()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        binding.trackingMapMv.onLowMemory()
        super.onLowMemory()
    }

    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return true
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = showSnackbar(
            binding.root,
            requireContext(),
            false,
            getString(R.string.no_internet_error),
            Snackbar.LENGTH_INDEFINITE
        )

        viewModel.networkState.collectLatest {
            if (it) {
                internetConnectionFlag = true
                snackBar.dismiss()
                setStartButtonsForNoInternetConnection(it)
            } else {
                internetConnectionFlag = false
                snackBar.show()
                setStartButtonsForNoInternetConnection(it)
            }
        }
    }
    
}