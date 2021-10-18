package com.ferechamitbeyli.keeprun.presentation.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentOnboardingBinding
import com.ferechamitbeyli.keeprun.framework.common.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.keeprun.presentation.view.activities.auth_activity.adapters.OnboardingAdapter
import com.ferechamitbeyli.keeprun.presentation.view.base.BaseFragment
import com.ferechamitbeyli.keeprun.presentation.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfFirstUse()

    }

    private fun checkIfFirstUse() {
        viewModel.getIfOnboardingFinished()
        viewModel.isOnboardingFinished.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
                hideKeyboard()

            } else {
                setupPager()
            }
        })
    }

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