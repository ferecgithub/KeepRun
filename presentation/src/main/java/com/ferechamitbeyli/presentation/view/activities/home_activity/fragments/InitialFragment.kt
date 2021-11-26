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
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions
import com.ferechamitbeyli.presentation.utils.states.EventState
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

        setupBottomNavigationViewVisibility()

        checkInternetConnection()

        listenEventChannel()

        getUsernameFromRemoteDB()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.submitWeightBtn.setOnClickListener {
            if (internetConnectionFlag) {
                viewModel.saveWeightInformation(binding.initialWeightEt.text.toString().toDouble())
                    .also {
                        navigateToRunsFragment()
                    }
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

    private fun listenEventChannel() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.initialEventsChannel.collectLatest {
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
                    /** NO-OP **/
                }
            }
        }
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = UIHelperFunctions.showSnackbar(
            binding.root,
            requireContext(),
            false,
            "No Internet Connection",
            Snackbar.LENGTH_INDEFINITE
        )
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