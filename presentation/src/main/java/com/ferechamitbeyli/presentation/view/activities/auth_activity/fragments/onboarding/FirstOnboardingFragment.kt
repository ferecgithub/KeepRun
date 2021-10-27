package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentFirstOnboardingBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstOnboardingFragment : BaseFragment<FragmentFirstOnboardingBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFirstOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.onboardingPager_vp2)

        binding.onboardingNextBtn.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }





}