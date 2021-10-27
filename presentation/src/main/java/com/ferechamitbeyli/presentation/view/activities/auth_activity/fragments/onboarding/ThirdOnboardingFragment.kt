package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentThirdOnboardingBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdOnboardingFragment : BaseFragment<FragmentThirdOnboardingBinding>() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentThirdOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getStartedBtn.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.storeFirstUseState(true)
                findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
            }
        }

    }





}