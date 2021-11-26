package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentRunsBinding
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.view.activities.home_activity.adapters.RunsAdapter
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.RunsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RunsFragment : BaseFragment<FragmentRunsBinding>() {

    private val viewModel: RunsViewModel by viewModels()

    private lateinit var runsAdapter: RunsAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRunsBinding = FragmentRunsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        clearFocusFromSpinner()

        checkCacheForWeightValue()

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        requireActivity().findViewById<FloatingActionButton>(R.id.add_run_fab).setOnClickListener {
            checkPermissionsBeforeNavigateToTrackingFragment()
        }
    }

    private fun adjustSortingSpinner() {
        val sortingOptions = resources.getStringArray(R.array.sorting_options)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.runs_sort_spinner_item, sortingOptions)
        binding.runsSortSp.setAdapter(arrayAdapter)
    }

    private fun setupRecyclerView() = binding.runsListRv.apply {
        runsAdapter = RunsAdapter()
        adapter = runsAdapter
        layoutManager = LinearLayoutManager(requireContext())

        handleEmptyRecyclerView()
    }

    private fun handleEmptyRecyclerView() {
        if (runsAdapter.checkIfListIsEmpty()) {
            binding.runsSortTil.visible(false)
            binding.runsListRv.visible(false)
        } else {
            binding.runsSortTil.visible(true)
            binding.runsListRv.visible(true)
        }
    }

    private fun clearFocusFromSpinner() {
        binding.runsSortSp.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        adjustSortingSpinner()
    }

    private fun navigateToInitialFragment() {

        if (findNavController().currentDestination?.id == R.id.runsFragment) {
            findNavController().navigate(R.id.action_runsFragment_to_initialFragment)
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

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.runsFragment) {
            findNavController().navigate(R.id.action_runsFragment_to_trackingFragment)
        }
    }

    private fun navigateToLocationPermissionFragment() {
        if (findNavController().currentDestination?.id == R.id.runsFragment) {
            findNavController().navigate(R.id.action_runsFragment_to_locationPermissionFragment)
        }
    }

    private fun checkCacheForWeightValue() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserWeightFromCache().collectLatest {
                if (it == 0.0) {
                    checkRemoteDBFromWeightValue()
                } else {
                    setupRecyclerView()
                }
            }
        }

    private fun checkRemoteDBFromWeightValue() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserWeightFromRemoteDB().collectLatest {
                if (it == 0.0) {
                    navigateToInitialFragment()
                } else {
                    setupRecyclerView()
                }
            }
        }
}