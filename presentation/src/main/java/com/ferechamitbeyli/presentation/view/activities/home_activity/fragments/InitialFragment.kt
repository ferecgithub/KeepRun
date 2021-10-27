package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ferechamitbeyli.presentation.databinding.FragmentInitialBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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