package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        checkRemoteDBForWeightValue()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.submitWeightBtn.setOnClickListener {
            viewModel.saveWeightInformation(binding.initialWeightEt.text.toString().toDouble())
            navigateToRunsFragment()
        }
    }

    private fun checkRemoteDBForWeightValue() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserWeightFromRemoteDB().collectLatest {
                Toast.makeText(requireContext(), "AGIRLIK : $it", Toast.LENGTH_SHORT).show()
                if (it != 0.0 && !it.isNaN()) {
                    navigateToRunsFragment()
                } else {
                    getUsernameFromRemoteDB()
                }
            }
            /*
            viewModel.userWeightFlow.collectLatest {
                logcat("AGIRLIK") { "DEĞERI = $it" }
                if (it != 0.0 && !it.isNaN()) {
                    navigateToRunsFragment()
                } else {
                    getUsernameFromRemoteDB()
                }
            }

             */

        }

    private fun navigateToRunsFragment() {
        if (findNavController().currentDestination?.id == R.id.initialFragment) {
            findNavController().navigate(R.id.action_initialFragment_to_runsFragment)
        }
    }

    private fun getUsernameFromRemoteDB() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUsernameFromRemoteDB().collectLatest {
                if (it.isNotBlank()) {
                    Toast.makeText(requireContext(), "USERNAME : $it", Toast.LENGTH_SHORT).show()
                    binding.welcomeUsernameTv.text = it
                } else {
                    Toast.makeText(requireContext(), "USERNAME is BLANK : $it", Toast.LENGTH_SHORT).show()
                }

            }
            /*
            viewModel.usernameFlow.collectLatest {
                binding.welcomeUsernameTv.text = it
            }

             */
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