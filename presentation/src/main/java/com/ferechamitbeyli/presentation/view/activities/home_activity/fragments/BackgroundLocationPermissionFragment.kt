package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentBackgroundLocationPermissionBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.showSnackbar
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class BackgroundLocationPermissionFragment :
    BaseFragment<FragmentBackgroundLocationPermissionBinding>(),
    EasyPermissions.PermissionCallbacks {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBackgroundLocationPermissionBinding =
        FragmentBackgroundLocationPermissionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.acceptBackgroundLocationPermissionBtn.setOnClickListener {
            if (PermissionManager.hasBackgroundLocationPermission(requireContext())) {
                navigateToTrackingFragment()
                showSnackbar(
                    binding.root,
                    requireContext(),
                    true,
                    "All set! You are ready to run.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                PermissionManager.requestBackgroundLocationPermission(this)
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
        if (findNavController().currentDestination?.id == R.id.backgroundLocationPermissionFragment) {
            findNavController().navigate(R.id.action_backgroundLocationPermissionFragment_to_trackingFragment)
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
            PermissionManager.requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        navigateToTrackingFragment()
        showSnackbar(
            binding.root,
            requireContext(),
            true,
            "All set! You are ready to run.",
            Snackbar.LENGTH_LONG
        ).show()
    }


}