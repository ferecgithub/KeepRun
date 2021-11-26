package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentThirdOnboardingBinding
import com.ferechamitbeyli.presentation.utils.helpers.AnimationHelperFunctions.setImageDrawableWithAnimation
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ThirdOnboardingFragment : BaseFragment<FragmentThirdOnboardingBinding>() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentThirdOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerIndicators()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.getStartedBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.storeFirstUseState(false)
                navigateToSignInFragment()
            }
        }
    }

    private fun setupPagerIndicators() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.currentPagerPosition.collect {
            binding.onboardingPagerIndicatorLayout.indicatorOne.setImageDrawableWithAnimation(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_indicator_default)!!
            )
            binding.onboardingPagerIndicatorLayout.indicatorTwo.setImageDrawableWithAnimation(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_indicator_default)!!
            )
            binding.onboardingPagerIndicatorLayout.indicatorThree.setImageDrawableWithAnimation(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_indicator_selected)!!
            )
        }
    }

    private fun navigateToSignInFragment() {
        if (findNavController().currentDestination?.id == R.id.onboardingFragment) {
            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
        }
    }


}
