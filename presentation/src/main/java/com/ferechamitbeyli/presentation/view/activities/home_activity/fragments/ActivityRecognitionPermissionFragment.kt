package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentActivityRecognitionPermissionBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager.hasActivityRecognitionPermission
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager.requestActivityRecognitionPermission
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.showSnackbar
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityRecognitionPermissionFragment :
    BaseFragment<FragmentActivityRecognitionPermissionBinding>(),
    EasyPermissions.PermissionCallbacks {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentActivityRecognitionPermissionBinding =
        FragmentActivityRecognitionPermissionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.acceptActivityRecognitionPermissionBtn.setOnClickListener {
            if (hasActivityRecognitionPermission(requireContext())) {
                navigateToBackgroundLocationPermissionFragment()
            } else {
                requestActivityRecognitionPermission(this)
            }
        }

        binding.backToRunsFragmentBtn.setOnClickListener {
            navigateToTrackingFragment()
            showSnackbar(
                binding.root,
                requireContext(),
                false,
                "You need to give permissions in order to use the app.",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.activityRecognitionPermissionFragment) {
            findNavController().navigate(R.id.action_activityRecognitionPermissionFragment_to_trackingFragment)
        }
    }

    private fun navigateToBackgroundLocationPermissionFragment() {
        if (findNavController().currentDestination?.id == R.id.activityRecognitionPermissionFragment) {
            findNavController().navigate(R.id.action_activityRecognitionPermissionFragment_to_backgroundLocationPermissionFragment)
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
            requestActivityRecognitionPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        navigateToBackgroundLocationPermissionFragment()
    }

}