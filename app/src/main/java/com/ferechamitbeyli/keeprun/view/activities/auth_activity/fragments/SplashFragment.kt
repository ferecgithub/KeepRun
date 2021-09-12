package com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentSplashBinding
import com.ferechamitbeyli.keeprun.model.local.cache.DataStoreObject
import com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(), CoroutineScope {

    private val viewModel: AuthViewModel by viewModels()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    @Inject
    lateinit var dataStoreObject: DataStoreObject

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            dataStoreObject.getIsFirstTime().collect {
                withContext(Dispatchers.Main) {
                    view.post{
                        if (it) {
                            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                        } else {
                            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                        }
                    }
                }

            }
        }



    }



}