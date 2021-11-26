package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentActivityRecognitionPermissionBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager.hasActivityRecognitionPermission
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager.requestActivityRecognitionPermission
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.HomeViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityRecognitionPermissionFragment :
    BaseFragment<FragmentActivityRecognitionPermissionBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val viewModel: HomeViewModel by viewModels()

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (hasActivityRecognitionPermission(requireContext())) {
                    viewModel.storeActivityRecognitionPermissionState(true)
                    navigateToTrackingFragment()
                } else {
                    requestActivityRecognitionPermission(this)
                }
            } else {
                navigateToTrackingFragment()
            }
        }

        binding.backToRunsFragmentBtn.setOnClickListener {
            navigateToRunsFragment()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.activityRecognitionPermissionFragment) {
            findNavController().navigate(R.id.action_activityRecognitionPermissionFragment_to_trackingFragment)
        }
    }

    private fun navigateToRunsFragment() {
        if (findNavController().currentDestination?.id == R.id.activityRecognitionPermissionFragment) {
            findNavController().navigate(R.id.action_activityRecognitionPermissionFragment_to_runsFragment)
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                SettingsDialog.Builder(requireActivity()).build().show()
            } else {
                requestActivityRecognitionPermission(this)
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.storeActivityRecognitionPermissionState(true)
            navigateToTrackingFragment()
        }
    }

}