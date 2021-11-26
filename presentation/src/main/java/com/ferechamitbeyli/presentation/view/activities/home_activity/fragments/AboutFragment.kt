package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentAboutBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding = FragmentAboutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        requireActivity().findViewById<FloatingActionButton>(R.id.add_run_fab).setOnClickListener {
            checkPermissionsBeforeNavigateToTrackingFragment()
        }
    }

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.aboutFragment) {
            findNavController().navigate(R.id.action_aboutFragment_to_trackingFragment)
        }
    }

    private fun navigateToLocationPermissionFragment() {
        if (findNavController().currentDestination?.id == R.id.aboutFragment) {
            findNavController().navigate(R.id.action_aboutFragment_to_locationPermissionFragment)
        }
    }

    private fun checkPermissionsBeforeNavigateToTrackingFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (PermissionManager.hasLocationPermission(requireContext()) &&
                PermissionManager.hasActivityRecognitionPermission(requireContext())
            ) {
                navigateToTrackingFragment()
            } else {
                navigateToLocationPermissionFragment()
            }
        } else {
            if (PermissionManager.hasLocationPermission(requireContext())) {
                navigateToTrackingFragment()
            } else {
                navigateToLocationPermissionFragment()
            }
        }
    }


}