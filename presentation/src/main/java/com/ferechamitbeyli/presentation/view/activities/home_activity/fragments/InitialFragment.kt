package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ferechamitbeyli.presentation.databinding.FragmentInitialBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import logcat.logcat

@AndroidEntryPoint
class InitialFragment : BaseFragment<FragmentInitialBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentInitialBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            getUserMailFromCache()
            getUserUidFromCache()
            getUsernameFromCache()
            getUserNotificationStateFromCache()
            getUserPhotoUrlFromCache()
        }
    }

    private suspend fun getUserUidFromCache() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.getUserUidFromCache()
        viewModel.userUidFlow.collectLatest {
            logcat("FEREC") { "USERUID $it" }
        }
    }

    private suspend fun getUserMailFromCache() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.getUserMailFromCache()
        viewModel.userMailFlow.collectLatest {
            logcat("FEREC") { "FEREC USERMAIL $it" }
        }
    }

    private suspend fun getUsernameFromCache() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.getUsernameFromCache()
        viewModel.usernameFlow.collectLatest {
            logcat("FEREC") { "FEREC USERNAME $it" }
        }
    }

    private suspend fun getUserNotificationStateFromCache() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.getUserNotificationStateFromCache()
        viewModel.userNotificationStateFlow.collectLatest {
            logcat("FEREC") { "USERNOTIFICATION ${it.toString()}" }
        }
    }

    private suspend fun getUserPhotoUrlFromCache() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.getUserPhotoUrlFromCache()
        viewModel.userPhotoUrlFlow.collectLatest {
            logcat("FEREC") { "FEREC USERPHOTOURL $it" }
        }
    }





    /*
    private fun getCurrentUser() {
        viewModel.getCurrentUser()
    }

    private fun listenToChannel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allEventsFlow.collect { event ->
                    when(event){
                        is AuthViewModel.AllEvents.Message ->{
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    /*
    private fun registerObserver() {
        viewModel.currentUser.observe(viewLifecycleOwner,{ user ->
            user?.let {
                binding
                binding?.apply{
                    welcomeTxt.text = "welcome ${it.email}"
                    signinButton.text = "sign out"
                    signinButton.setOnClickListener {
                        viewModel.signOut()
                    }
                }
            }?: binding?.apply {
                welcomeTxt.isVisible = false
                signinButton.text = "sign in"
                signinButton.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
                }
            }
        })
    }

     */



     */

}