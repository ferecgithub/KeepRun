package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentSecondOnboardingBinding
import com.ferechamitbeyli.presentation.utils.helpers.AnimationHelperFunctions.setImageDrawableWithAnimation
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SecondOnboardingFragment : BaseFragment<FragmentSecondOnboardingBinding>() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSecondOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.onboardingPager_vp2)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentPagerPosition.collect {
                binding.onboardingPagerIndicatorLayout.indicatorOne.setImageDrawableWithAnimation(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_indicator_default)!!
                )
                binding.onboardingPagerIndicatorLayout.indicatorTwo.setImageDrawableWithAnimation(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_indicator_selected)!!
                )
                binding.onboardingPagerIndicatorLayout.indicatorThree.setImageDrawableWithAnimation(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_indicator_default)!!
                )
            }
        }

        binding.onboardingNextBtn.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }





}