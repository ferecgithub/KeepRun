package com.ferechamitbeyli.keeprun.presentation.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentSecondOnboardingBinding
import com.ferechamitbeyli.keeprun.presentation.view.base.BaseFragment
import com.ferechamitbeyli.keeprun.presentation.viewmodel.activities.auth_activity.AuthViewModel

class SecondOnboardingFragment : BaseFragment<FragmentSecondOnboardingBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSecondOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.onboardingPager_vp2)

        binding.onboardingNextBtn.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }





}