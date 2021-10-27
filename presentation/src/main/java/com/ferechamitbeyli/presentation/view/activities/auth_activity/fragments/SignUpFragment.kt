package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentSignUpBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.signUpToSignInTv.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

    }

}