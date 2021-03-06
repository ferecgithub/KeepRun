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
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.utils.helpers.ZoomOutPageTransformer
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.view.activities.auth_activity.adapters.OnboardingAdapter
import com.ferechamitbeyli.presentation.view.activities.home_activity.HomeActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding.OnboardingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCachedEmailThenFirstUseState()
        listenOnboardingEventChannel()
    }

    private fun listenOnboardingEventChannel() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.onboardingEventsFlow.collectLatest {
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
                    else -> {
                        /** NO-OP **/
                    }
                }
            }

        }

    private fun navigateToSignInFragment() {
        if (findNavController().currentDestination?.id == R.id.onboardingFragment) {
            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
        }
    }

    private fun navigateToHomeActivity() {
        if (findNavController().currentDestination?.id == R.id.onboardingFragment) {
            requireActivity().startNewActivity(HomeActivity::class.java)
        }
    }

    private fun checkCachedEmailThenFirstUseState() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            combine(
                viewModel.getUserEmailFromCache(),
                viewModel.getFirstUseState(),
            ) { email, isFirstUse ->

                if (email.isNotBlank()) {
                    navigateToHomeActivity()
                }
                if (isFirstUse) {
                    setupPager()
                } else {
                    navigateToSignInFragment()
                    hideKeyboard()
                }

            }.collect()
        }

    private fun setupPager() {
        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingFragment(),
            SecondOnboardingFragment(),
            ThirdOnboardingFragment()
        )

        val adapter = OnboardingAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )

        binding.onboardingPagerVp2.setPageTransformer(ZoomOutPageTransformer())
        binding.onboardingPagerVp2.adapter = adapter

        // Set viewPager indicator
        when (binding.onboardingPagerVp2.currentItem) {
            0 -> {
                viewModel.assignPagerPosition(0)
            }
            1 -> {
                viewModel.assignPagerPosition(1)
            }
            else -> {
                viewModel.assignPagerPosition(2)
            }
        }
    }

}