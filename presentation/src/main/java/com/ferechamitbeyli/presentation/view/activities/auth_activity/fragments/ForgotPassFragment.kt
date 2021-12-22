package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentForgotPassBinding
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForgotPassFragment : BaseFragment<FragmentForgotPassBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    private var validEmailFlag: Boolean = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForgotPassBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternetConnection()

        // Disable Send Button for Validation
        enableSendButtonIfAllValid(validEmailFlag)

        addTextChangeListenersToFields()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.forgotPassSendBtn.setOnClickListener {
            if (validEmailFlag and internetConnectionFlag) {
                viewModel.sendPasswordResetEmail(binding.forgotPassEmailEt.text.toString())
            }
            listenEventChannel()
            hideKeyboard()
        }

        binding.forgotPassBackBtn.setOnClickListener {
            navigateToSignInFragment()
        }
    }

    private fun navigateToSignInFragment() {
        if (findNavController().currentDestination?.id == R.id.forgotPassFragment) {
            findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)
        }
    }

    private fun enableSendButtonIfAllValid(emailFlag: Boolean) {
        if (emailFlag) {
            binding.forgotPassSendBtn.enable(true)
        } else {
            binding.forgotPassSendBtn.enable(false)
        }
    }

    private fun addTextChangeListenersToFields() {
        binding.forgotPassEmailEt.addTextChangedListener {
            viewModel.validateBeforePasswordReset(
                binding.forgotPassEmailEt.text.toString()
            )
            listenValidationChannel()
        }
    }

    private fun listenValidationChannel() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.authValidationEventsChannel.collect {
            when (it) {
                is ValidationState.ValidationError -> {
                    when (it.result) {
                        ValidationErrorResults.EMPTY_EMAIL -> {
                            binding.forgotPassEmailEt.error = getString(R.string.email_empty_error)
                            validEmailFlag = false
                            enableSendButtonIfAllValid(validEmailFlag)
                        }
                        ValidationErrorResults.INVALID_EMAIL -> {
                            binding.forgotPassEmailEt.error =
                                getString(R.string.email_invalid_error)
                            validEmailFlag = false
                            enableSendButtonIfAllValid(validEmailFlag)
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
                is ValidationState.ValidationSuccess -> {
                    when (it.result) {
                        ValidationResults.VALID_EMAIL -> {
                            validEmailFlag = true
                            enableSendButtonIfAllValid(validEmailFlag)
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
            }
        }
    }

    private fun listenEventChannel() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.authEventsChannel.collect {
            when (it) {
                is EventState.Error -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                    setAvailabilityOfForgotPassButtons(true)
                }
                is EventState.Loading -> {
                    setAvailabilityOfForgotPassButtons(false)
                }
                is EventState.Success -> {
                    setAvailabilityOfForgotPassButtons(true)
                    navigateToSignInFragment()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.confirmation_sent_to_email),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setAvailabilityOfForgotPassButtons(isEnabled: Boolean) {
        binding.forgotPassSendBtn.enable(isEnabled)
        binding.forgotPassBackBtn.enable(isEnabled)
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar =
            Snackbar.make(
                binding.root,
                getString(R.string.no_internet_error),
                Snackbar.LENGTH_INDEFINITE
            )
        viewModel.networkState.collect {
            if (it) {
                internetConnectionFlag = true
                snackBar.dismiss()
            } else {
                internetConnectionFlag = false
                snackBar.show()
                setAvailabilityOfForgotPassButtons(true)
            }
        }
    }

}