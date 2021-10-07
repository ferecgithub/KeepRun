package com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentFirstOnboardingBinding
import com.ferechamitbeyli.keeprun.view.base.BaseFragment
import com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity.AuthViewModel

class FirstOnboardingFragment : BaseFragment<FragmentFirstOnboardingBinding>() {

    private val viewModel: AuthViewModel by viewModels()

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