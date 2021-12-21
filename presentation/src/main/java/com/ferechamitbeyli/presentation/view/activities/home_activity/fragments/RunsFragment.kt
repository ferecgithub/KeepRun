package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentRunsBinding
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.uimodels.mappers.RunToUIMapper
import com.ferechamitbeyli.presentation.utils.enums.RunSortType
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.visible
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.view.activities.home_activity.adapters.RunsAdapter
import com.ferechamitbeyli.presentation.view.activities.home_activity.adapters.SwipeToDeleteCallback
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.RunsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RunsFragment : BaseFragment<FragmentRunsBinding>() {

    private val viewModel: RunsViewModel by viewModels()

    private var runsAdapter: RunsAdapter? = null

    private var runsList: MutableList<RunUIModel>? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRunsBinding = FragmentRunsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternetConnection()

        setupBottomNavigationViewVisibility()

        clearFocusFromSpinner()

        checkCacheForWeightValue()

        listenEventChannel()

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
            ArrayAdapter(requireContext(), R.layout.spinner_item, sortingOptions)
        binding.runsSortSp.setAdapter(arrayAdapter)

        selectInitialSortTypeIfEmpty()

        setSelectionPerRunSortType()

        binding.runsSortSp.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> viewModel.sortRuns(RunSortType.DATE)
                1 -> viewModel.sortRuns(RunSortType.RUN_TIME)
                2 -> viewModel.sortRuns(RunSortType.CALORIES_BURNED)
                3 -> viewModel.sortRuns(RunSortType.DISTANCE)
                4 -> viewModel.sortRuns(RunSortType.AVG_SPEED)
                5 -> viewModel.sortRuns(RunSortType.STEP_COUNT)
                else -> {
                    /** NO-OP **/
                }
            }
        }

    }

    private fun selectInitialSortTypeIfEmpty() {
        if (!binding.runsSortSp.hasSelection()) {
            binding.runsSortSp.setText(binding.runsSortSp.adapter.getItem(0).toString(), false)
        }
    }

    private fun setSelectionPerRunSortType() {
        when (viewModel.runSortType) {
            RunSortType.DATE -> binding.runsSortSp.setSelection(0)
            RunSortType.RUN_TIME -> binding.runsSortSp.setSelection(1)
            RunSortType.CALORIES_BURNED -> binding.runsSortSp.setSelection(2)
            RunSortType.DISTANCE -> binding.runsSortSp.setSelection(3)
            RunSortType.AVG_SPEED -> binding.runsSortSp.setSelection(4)
            RunSortType.STEP_COUNT -> binding.runsSortSp.setSelection(5)
        }
    }

    private fun setupRecyclerView() =
        binding.runsListRv.apply {
            runsAdapter = RunsAdapter()
            adapter = runsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)


            val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    runsList?.get(position)?.let { viewModel.removeRunFromSynced(it) }
                    this@apply.adapter?.notifyItemChanged(position)
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this@apply)


        }

    private fun getRuns() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.runsFlow.collectLatest { runsFromDB ->
            if (runsFromDB.isEmpty()) {
                viewModel.getAllRunsFromRemoteDatabase().collect()
                viewModel.sortRuns(viewModel.runSortType)
                populateRecyclerView(RunToUIMapper.mapFromDomainModelList(runsFromDB))
            } else {
                populateRecyclerView(RunToUIMapper.mapFromDomainModelList(runsFromDB))
            }

        }
    }

    private fun populateRecyclerView(list: List<RunUIModel>) {
        runsList = list.toMutableList()
        runsAdapter?.submitList(list)
        handleEmptyRecyclerView(list)
    }

    private fun handleEmptyRecyclerView(list: List<Any>) {
        if (list.isNullOrEmpty()) {
            binding.runsSortTil.visible(true)
            binding.runsListRv.visible(false)
            binding.runsEmptyIv.visible(true)
            binding.runsEmptyIv.alpha = 0.5f
            binding.runsEmptyTv.visible(true)
        } else {
            binding.runsSortTil.visible(true)
            binding.runsListRv.visible(true)
            binding.runsEmptyIv.visible(false)
            binding.runsEmptyTv.visible(false)
        }
    }

    private fun clearFocusFromSpinner() {
        binding.runsSortSp.clearFocus()
    }

    private fun navigateToInitialFragment() {

        if (findNavController().currentDestination?.id == R.id.runsFragment) {
            findNavController().navigate(R.id.action_runsFragment_to_initialFragment)
        }
    }

    private fun checkPermissionsBeforeNavigateToTrackingFragment() {
        if (PermissionManager.hasLocationPermission(requireContext())) {
            navigateToTrackingFragment()
        } else {
            navigateToLocationPermissionFragment()
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
                    getRuns()
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
                    getRuns()
                }
            }
        }

    private fun listenEventChannel() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.runEventsChannel.collectLatest {
            when (it) {
                is EventState.Error -> {
                    UIHelperFunctions.showSnackbar(
                        binding.root,
                        requireContext(),
                        false,
                        it.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is EventState.Loading -> {
                    /** NO-OP **/
                }
                is EventState.Success -> {
                    UIHelperFunctions.showSnackbar(
                        binding.root,
                        requireContext(),
                        true,
                        it.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adjustSortingSpinner()
    }

    override fun onDestroyView() {
        runsAdapter = null
        super.onDestroyView()
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = UIHelperFunctions.showSnackbar(
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
            } else {
                internetConnectionFlag = false
                snackBar.show()
            }
        }
    }
}