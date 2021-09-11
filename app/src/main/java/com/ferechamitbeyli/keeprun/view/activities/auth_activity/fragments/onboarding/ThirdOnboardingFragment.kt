package com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentThirdOnboardingBinding
import com.ferechamitbeyli.keeprun.model.local.cache.DataStoreObject
import com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments.BaseFragment
import com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ThirdOnboardingFragment : BaseFragment<FragmentThirdOnboardingBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var dataStoreObject: DataStoreObject

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentThirdOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getStartedBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dataStoreObject.storeIfFirstTimeUse(false)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
                }
            }

        }

    }





}