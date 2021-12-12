package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentStatisticsBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatisticsBinding = FragmentStatisticsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        setupOnClickListeners()

    }

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.statisticsFragment) {
            findNavController().navigate(R.id.action_statisticsFragment_to_trackingFragment)
        }
    }

    private fun setupOnClickListeners() {
        requireActivity().findViewById<FloatingActionButton>(R.id.add_run_fab).setOnClickListener {
            checkPermissionsBeforeNavigateToTrackingFragment()
        }
    }

    private fun navigateToLocationPermissionFragment() {
        if (findNavController().currentDestination?.id == R.id.statisticsFragment) {
            findNavController().navigate(R.id.action_statisticsFragment_to_locationPermissionFragment)
        }
    }

    private fun checkPermissionsBeforeNavigateToTrackingFragment() {
        if (PermissionManager.hasLocationPermission(requireContext())) {
            navigateToTrackingFragment()
        } else {
            navigateToLocationPermissionFragment()
        }
    }
}