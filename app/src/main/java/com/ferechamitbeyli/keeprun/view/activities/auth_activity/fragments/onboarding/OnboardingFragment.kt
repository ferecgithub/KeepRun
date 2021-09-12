package com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentOnboardingBinding
import com.ferechamitbeyli.keeprun.model.local.cache.DataStoreObject
import com.ferechamitbeyli.keeprun.view.activities.auth_activity.adapters.OnboardingAdapter
import com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments.BaseFragment
import com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var dataStoreObject: DataStoreObject

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfFirstUse()

    }


    private fun checkIfFirstUse() {
        viewModel.getIfFirstUse()
        viewModel.isFirstUse.observe(viewLifecycleOwner, {
            if (it) {
                setupPager()
            } else {
                findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
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

        binding.onboardingPagerVp.adapter = adapter

    }


}