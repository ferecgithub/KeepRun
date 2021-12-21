package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

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
import com.ferechamitbeyli.presentation.databinding.FragmentStatisticsBinding
import com.ferechamitbeyli.presentation.uimodels.StatisticsUIModel
import com.ferechamitbeyli.presentation.utils.enums.DateType
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.visible
import com.ferechamitbeyli.presentation.view.activities.home_activity.adapters.StatisticsAdapter
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.StatisticsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    private val viewModel: StatisticsViewModel by viewModels()

    private var statisticsAdapter: StatisticsAdapter? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatisticsBinding = FragmentStatisticsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        checkInternetConnection()

        setupOnClickListeners()


        getDays()

        getDays(-3)


    }



    private fun setupRecyclerView() =
        binding.statisticsRv.apply {
            statisticsAdapter = StatisticsAdapter()
            adapter = statisticsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

    private fun populateRecyclerView(list: List<StatisticsUIModel>) {
        statisticsAdapter?.submitList(list)
        handleEmptyRecyclerView(list)
    }

    private fun getStatistics() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {

    }

    private fun handleEmptyRecyclerView(list: List<Any>) {
        if (list.isNullOrEmpty()) {
            binding.statisticsDateDisplayTil.visible(true)
            binding.statisticsRv.visible(false)
            binding.runsEmptyIv.visible(true)
            binding.runsEmptyIv.alpha = 0.5f
            binding.runsEmptyTv.visible(true)
        } else {
            binding.statisticsDateDisplayTil.visible(true)
            binding.statisticsRv.visible(true)
            binding.runsEmptyIv.visible(false)
            binding.runsEmptyTv.visible(false)
        }
    }

    private fun adjustStatisticsDateSpinner() {
        val dateOptions = resources.getStringArray(R.array.date_options)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item, dateOptions)
        binding.statisticsDateDisplaySp.setAdapter(arrayAdapter)

        selectInitialDateTypeIfEmpty()

        setSelectionPerDateType()

        binding.statisticsDateDisplaySp.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> viewModel.displayStatisticsByDate(DateType.TODAY)
                1 -> viewModel.displayStatisticsByDate(DateType.THIS_WEEK)
                2 -> viewModel.displayStatisticsByDate(DateType.THIS_MONTH)
                else -> {
                    /** NO-OP **/
                }
            }
        }

    }

    private fun selectInitialDateTypeIfEmpty() {
        if (!binding.statisticsDateDisplaySp.hasSelection()) {
            binding.statisticsDateDisplaySp.setText(binding.statisticsDateDisplaySp.adapter.getItem(0).toString(), false)
        }
    }

    private fun setSelectionPerDateType() {
        when (viewModel.dateType) {
            DateType.TODAY -> binding.statisticsDateDisplaySp.setSelection(0)
            DateType.THIS_WEEK -> binding.statisticsDateDisplaySp.setSelection(1)
            DateType.THIS_MONTH -> binding.statisticsDateDisplaySp.setSelection(2)
        }
    }

    private fun getDays(number: Int = 0) {
        val calendar1 = Calendar.getInstance().apply {
            timeInMillis = StatisticsHelperFunctions.getTimestampOfToday()[0]
        }
        val calendar2 = Calendar.getInstance().apply {
            timeInMillis = StatisticsHelperFunctions.getTimestampOfToday()[1]
        }

        val dateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm:ss a", Locale.getDefault())
        println("**********************")
        if (number == 0) {
            println("THIS_WEEK_START DATE IS: ${dateFormat.format(calendar1.time)}")
            println("THIS_WEEK_END DATE IS: ${dateFormat.format(calendar2.time)}")
        } else {
            println("START DATE IS: ${dateFormat.format(calendar1.time)}")
            println("END DATE IS: ${dateFormat.format(calendar2.time)}")
        }
        println("**********************")
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

    override fun onResume() {
        super.onResume()
        adjustStatisticsDateSpinner()
    }
}