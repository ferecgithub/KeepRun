package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentRunsBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunsFragment : BaseFragment<FragmentRunsBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRunsBinding = FragmentRunsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigationViewIfCurrentFragmentIsInitialFragment()
    }

    private fun adjustSortingSpinner() {
        val sortingOptions = resources.getStringArray(R.array.sorting_options)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.runs_sort_spinner_item, sortingOptions)
        binding.runsSortSp.setAdapter(arrayAdapter)
    }

    override fun onResume() {
        super.onResume()
        adjustSortingSpinner()
    }
}