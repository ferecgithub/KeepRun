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
import com.ferechamitbeyli.presentation.databinding.FragmentSignUpBinding
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
import kotlinx.coroutines.flow.collectLatest
import logcat.logcat

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    private var validUsernameFlag: Boolean = false
    private var validEmailFlag: Boolean = false
    private var validPasswordFlag: Boolean = false
    private var validConfirmPasswordFlag: Boolean = false
    private var internetConnectionFlag: Boolean = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternetConnection()

        // Disable Sign Up Button for Validation
        enableSignUpButtonIfAllValid(
            validUsernameFlag,
            validEmailFlag,
            validPasswordFlag,
            validConfirmPasswordFlag
        )

        addTextChangeListenersToFields()

        binding.signUpBtn.setOnClickListener {
            if (validUsernameFlag and validEmailFlag and validPasswordFlag and validConfirmPasswordFlag and internetConnectionFlag) {
                viewModel.signUpUser(
                    binding.signUpEmailEt.text.toString(),
                    binding.signUpPasswordEt.text.toString(),
                    binding.signUpUsernameEt.text.toString()
                )
            }
            listenEventChannel()
            hideKeyboard()
        }

        binding.signUpToSignInTv.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

    }

    private fun enableSignUpButtonIfAllValid(
        usernameFlag: Boolean,
        emailFlag: Boolean,
        passwordFlag: Boolean,
        confirmPasswordFlag: Boolean
    ) {
        if (usernameFlag and emailFlag and passwordFlag and confirmPasswordFlag) {
            binding.signUpBtn.enable(true)
        } else {
            binding.signUpBtn.enable(false)
        }
    }

    private fun addTextChangeListenersToFields() {
        binding.signUpUsernameEt.addTextChangedListener {
            viewModel.validateBeforeSignUp(
                binding.signUpUsernameEt.text.toString(),
                binding.signUpEmailEt.text.toString(),
                binding.signUpPasswordEt.text.toString(),
                binding.signUpPassAgainEt.text.toString()
            )
            listenValidationChannel()
        }

        binding.signUpEmailEt.addTextChangedListener {
            viewModel.validateBeforeSignUp(
                binding.signUpUsernameEt.text.toString(),
                binding.signUpEmailEt.text.toString(),
                binding.signUpPasswordEt.text.toString(),
                binding.signUpPassAgainEt.text.toString()
            )
            listenValidationChannel()
        }

        binding.signUpPasswordEt.addTextChangedListener {
            logcat { "SIGNUPPASS : ${it.toString()}" }
            viewModel.validateBeforeSignUp(
                binding.signUpUsernameEt.text.toString(),
                binding.signUpEmailEt.text.toString(),
                binding.signUpPasswordEt.text.toString(),
                binding.signUpPassAgainEt.text.toString()
            )
            listenValidationChannel()
        }

        binding.signUpPassAgainEt.addTextChangedListener {
            logcat { "CONFIRMPASS : ${it.toString()}" }
            viewModel.validateBeforeSignUp(
                binding.signUpUsernameEt.text.toString(),
                binding.signUpEmailEt.text.toString(),
                binding.signUpPasswordEt.text.toString(),
                binding.signUpPassAgainEt.text.toString()
            )
            listenValidationChannel()
        }
    }

    private fun listenValidationChannel() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.authValidationEventsChannel.collectLatest {
            when (it) {
                is ValidationState.ValidationError -> {
                    logcat { "Validation Error message: ${it.result}" }
                    when (it.result) {
                        ValidationErrorResults.EMPTY_EMAIL -> {
                            binding.signUpEmailEt.error = "Please enter an email."
                            validEmailFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.INVALID_EMAIL -> {
                            binding.signUpEmailEt.error = "Invalid email."
                            validEmailFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.EMPTY_USERNAME -> {
                            binding.signUpUsernameEt.error = "Please enter a username."
                            validUsernameFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.USERNAME_TOO_LONG -> {
                            binding.signUpUsernameEt.error =
                                "Please enter a username shorter than 13 characters."
                            validUsernameFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.EMPTY_PASSWORD -> {
                            logcat { "EMPTY PASS, pass : ${binding.signUpPasswordEt.text.toString()} , confirmPass : ${binding.signUpPassAgainEt.text.toString()}" }
                            binding.signUpPasswordEt.error = "Please enter your password."
                            validPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.EMPTY_CONFIRM_PASSWORD -> {
                            logcat { "EMPTY PASS, pass : ${binding.signUpPasswordEt.text.toString()} , confirmPass : ${binding.signUpPassAgainEt.text.toString()}" }
                            binding.signUpPassAgainEt.error = "Please enter your password."
                            validConfirmPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.PASSWORD_CHAR_ERROR -> {
                            binding.signUpPasswordEt.error =
                                "Your password must be longer than 6 character and must contain at least one capital, one lower letter, and one special character."
                            validPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.PASSWORDS_NOT_MATCHED -> {
                            binding.signUpPassAgainEt.error = "The passwords are not matched."
                            validConfirmPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                    }
                }
                is ValidationState.ValidationSuccess -> {
                    logcat { "Validation Success message: ${it.result}" }
                    when (it.result) {
                        ValidationResults.VALID_USERNAME -> {
                            validUsernameFlag = true
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationResults.VALID_EMAIL -> {
                            validEmailFlag = true
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationResults.VALID_BOTH_PASSWORDS -> {
                            validConfirmPasswordFlag = true
                            validPasswordFlag = true
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
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
        viewModel.authEventsChannel.collectLatest {
            when (it) {
                is EventState.Error -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                }
                is EventState.Loading -> {
                    /** NO-OP **/
                }
                is EventState.Success -> {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    if (findNavController().currentDestination?.id == R.id.signUpFragment) {
                        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                    }
                }
            }
        }
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar =
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
        viewModel.networkState.collectLatest {
            if (it) {
                internetConnectionFlag = true
                snackBar.dismiss()
            } else {
                internetConnectionFlag = false
                snackBar.show()
            }
        }
    }

}