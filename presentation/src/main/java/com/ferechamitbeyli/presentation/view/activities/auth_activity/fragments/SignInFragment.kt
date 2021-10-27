package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentSignInBinding
import com.ferechamitbeyli.presentation.utils.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.view.activities.home_activity.HomeActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignInBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable SignIn Button for Validation
        binding.signInBtn.enable(false)

        binding.signInEmailEt.addTextChangedListener {
            val email = binding.signInEmailEt.text.toString().trim()
            binding.signInBtn.enable(email.isNotEmpty())
        }

        binding.signInBtn.setOnClickListener {
            requireActivity().startNewActivity(HomeActivity::class.java)
        }

        binding.forgotPassTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPassFragment)
        }

        binding.signUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

    }



}