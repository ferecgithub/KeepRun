package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentLocationPermissionBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager.hasLocationPermission
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager.requestLocationPermission
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.HomeViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationPermissionFragment : BaseFragment<FragmentLocationPermissionBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val viewModel: HomeViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLocationPermissionBinding =
        FragmentLocationPermissionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.acceptLocationPermissionBtn.setOnClickListener {
            if (hasLocationPermission(requireContext())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    viewModel.storeFineLocationPermissionState(true)
                    navigateToActivityRecognitionFragment()
                } else {
                    navigateToTrackingFragment()
                }
            } else {
                requestLocationPermission(this)
            }
        }

        binding.backToRunsFragmentBtn.setOnClickListener {
            navigateToRunsFragment()
        }
    }

    private fun navigateToActivityRecognitionFragment() {
        if (findNavController().currentDestination?.id == R.id.locationPermissionFragment) {
            findNavController().navigate(R.id.action_locationPermissionFragment_to_activityRecognitionPermissionFragment)
        }
    }

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.locationPermissionFragment) {
            findNavController().navigate(R.id.action_locationPermissionFragment_to_trackingFragment)
        }
    }

    private fun navigateToRunsFragment() {
        if (findNavController().currentDestination?.id == R.id.locationPermissionFragment) {
            findNavController().navigate(R.id.action_locationPermissionFragment_to_runsFragment)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel.storeFineLocationPermissionState(true)
            navigateToActivityRecognitionFragment()
        } else {
            navigateToTrackingFragment()
        }
    }


}