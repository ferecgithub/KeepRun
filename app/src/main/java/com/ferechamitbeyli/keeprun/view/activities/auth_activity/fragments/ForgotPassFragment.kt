package com.ferechamitbeyli.keeprun.view.activities.auth_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentForgotPassBinding
import com.ferechamitbeyli.keeprun.view.base.BaseFragment
import com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassFragment : BaseFragment<FragmentForgotPassBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForgotPassBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotPassSendBtn.setOnClickListener {
            Snackbar.make(view, "Confirmation mail has sent to your email address.", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)
        }

        binding.forgotPassBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)
        }

    }

}