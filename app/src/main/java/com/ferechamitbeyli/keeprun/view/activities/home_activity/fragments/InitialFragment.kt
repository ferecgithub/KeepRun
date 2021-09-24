package com.ferechamitbeyli.keeprun.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ferechamitbeyli.keeprun.databinding.FragmentInitialBinding
import com.ferechamitbeyli.keeprun.view.base.BaseFragment
import com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity.AuthViewModel
import com.ferechamitbeyli.keeprun.viewmodel.activities.home_activity.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InitialFragment : BaseFragment<FragmentInitialBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentInitialBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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