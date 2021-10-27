package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentOnboardingBinding
import com.ferechamitbeyli.presentation.utils.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.utils.enums.SessionResults
import com.ferechamitbeyli.presentation.utils.states.OnboardingState
import com.ferechamitbeyli.presentation.view.activities.auth_activity.adapters.OnboardingAdapter
import com.ferechamitbeyli.presentation.view.activities.home_activity.HomeActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding.OnboardingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentUser()
        viewModel.getFirstUseState()
        listenOnboardingEventChannel()
        /*
        if (viewModel.getCurrentUser().equals(true)) {
            listenOnboardingEventChannel()
        } else {
            viewModel.getFirstUseState().invokeOnCompletion { listenOnboardingEventChannel() }

        }

         */

    }

    private fun listenOnboardingEventChannel() = lifecycleScope.launchWhenStarted {
        viewModel.onboardingEventsFlow.collect {
            when(it) {
                is OnboardingState.Success -> {
                    when(it.result) {
                        SessionResults.FIRST_USE -> {
                            setupPager()
                        }
                        SessionResults.NOT_FIRST_USE -> {
                            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
                            hideKeyboard()
                        }
                        SessionResults.THERE_IS_A_CURRENT_USER -> {
                            requireActivity().startNewActivity(HomeActivity::class.java)
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
                is OnboardingState.Error -> {

                }
                OnboardingState.Loading -> {

                }
            }
        }
    }

    private fun getFirstUseState() = lifecycleScope.launchWhenStarted {
        viewModel.getFirstUseState()
        listenOnboardingEventChannel()
    }

    private fun getCurrentUser() = lifecycleScope.launchWhenStarted {
        viewModel.getCurrentUser()
        listenOnboardingEventChannel()
    }

    /*
    private suspend fun checkFirstUseState() = lifecycleScope.launchWhenStarted {
        viewModel.getFirstUseState()
        viewModel.firstUseState.collect {
            if (it == true) {
                findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
                hideKeyboard()
            } else {
                setupPager()
            }
        }
    }



    private suspend fun setupRedirection() {
        getCurrentUser()
    }

     */

    /*
    private fun checkInitialSetupState() = lifecycleScope.launchWhenStarted {
        viewModel.getInitialSetupState()
        viewModel.initialSetupState.collect {
            if (it == true) {
                requireActivity().startNewActivity(HomeActivity::class.java)
            }
        }
    }

     */

    /*
    private suspend fun getCurrentUser() = lifecycleScope.launchWhenStarted {
        viewModel.getCurrentUser()
        viewModel.currentUser.collect {
            it?.let {
                requireActivity().startNewActivity(HomeActivity::class.java)
            }
        }
    }

     */

    private fun setupPager() {
        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingFragment(),
            SecondOnboardingFragment(),
            ThirdOnboardingFragment()
        )

        val adapter = OnboardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.onboardingPagerVp2.adapter = adapter

        // Set viewPager indicator
        binding.onboardingPagerIndicatorCi3.setViewPager(binding.onboardingPagerVp2)


    }


}