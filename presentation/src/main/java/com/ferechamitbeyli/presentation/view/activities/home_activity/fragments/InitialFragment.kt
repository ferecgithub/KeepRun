package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentInitialBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.InitialViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class InitialFragment : BaseFragment<FragmentInitialBinding>() {

    private val viewModel: InitialViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentInitialBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternetConnection()

        hideBottomNavigationViewIfCurrentFragmentIsInitialFragment()

        checkCacheForWeightValue()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.submitWeightBtn.setOnClickListener {
            viewModel.saveWeightInformation(binding.initialWeightEt.text.toString().toDouble())
            navigateToRunsFragment()
        }
    }

    private fun checkCacheForWeightValue() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserWeightFromCache().collectLatest {
                if (it != 0.0) {
                    navigateToRunsFragment()
                } else {
                    checkRemoteDBFromWeightValue()
                }
            }
        }

    private fun checkRemoteDBFromWeightValue() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserWeightFromRemoteDB().collectLatest {
                if (it != 0.0) {
                    viewModel.saveWeightInformation(it)
                    navigateToRunsFragment()
                } else {
                    getUsernameFromRemoteDB()
                }
            }
        }

    private fun navigateToRunsFragment() {
        if (findNavController().currentDestination?.id == R.id.initialFragment) {
            findNavController().navigate(R.id.action_initialFragment_to_runsFragment)
        }
    }

    private fun getUsernameFromRemoteDB() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUsernameFromRemoteDB().collectLatest {
                binding.welcomeUsernameTv.text = it
            }
        }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar =
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
        viewModel.networkState.collect {
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