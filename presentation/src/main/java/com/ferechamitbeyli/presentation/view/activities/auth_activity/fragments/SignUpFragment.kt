package com.ferechamitbeyli.presentation.view.activities.auth_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentSignUpBinding
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    private var validUsernameFlag: Boolean = false
    private var validEmailFlag: Boolean = false
    private var validPasswordFlag: Boolean = false
    private var validConfirmPasswordFlag: Boolean = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpannableTextColorOfSignIn()

        checkInternetConnection()

        // Disable Sign Up Button for Validation
        enableSignUpButtonIfAllValid(
            validUsernameFlag,
            validEmailFlag,
            validPasswordFlag,
            validConfirmPasswordFlag
        )

        addTextChangeListenersToFields()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {

        binding.signUpBtn.setOnClickListener {
            signUpUser()
        }

        binding.signUpToSignInTv.setOnClickListener {
            navigateToSignInFragment()
        }

    }

    private fun signUpUser() {
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

    private fun navigateToSignInFragment() {
        if (findNavController().currentDestination?.id == R.id.signUpFragment) {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun setSpannableTextColorOfSignIn() {
        setSpannableTextColor(
            binding.signUpToSignInTv,
            getString(R.string.already_have_acc),
            getString(R.string.sign_in),
            ContextCompat.getColor(requireContext(), R.color.darkGreen)
        )
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
            viewModel.validateBeforeSignUp(
                binding.signUpUsernameEt.text.toString(),
                binding.signUpEmailEt.text.toString(),
                binding.signUpPasswordEt.text.toString(),
                binding.signUpPassAgainEt.text.toString()
            )
            listenValidationChannel()
        }

        binding.signUpPassAgainEt.addTextChangedListener {
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
        viewModel.authValidationEventsChannel.collectLatest { validationResult ->
            when (validationResult) {
                is ValidationState.ValidationError -> {
                    when (validationResult.result) {
                        ValidationErrorResults.EMPTY_EMAIL -> {
                            binding.signUpEmailEt.error = getString(R.string.email_empty_error)
                            validEmailFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.INVALID_EMAIL -> {
                            binding.signUpEmailEt.error = getString(R.string.email_invalid_error)
                            validEmailFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.EMPTY_USERNAME -> {
                            binding.signUpUsernameEt.error = getString(R.string.username_empty_error)
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
                                getString(R.string.username_too_long_error)
                            validUsernameFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.EMPTY_PASSWORD -> {
                            binding.signUpPasswordEt.error = getString(R.string.password_empty_error)
                            validPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.EMPTY_CONFIRM_PASSWORD -> {
                            binding.signUpPassAgainEt.error = getString(R.string.password_empty_error)
                            validConfirmPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.PASSWORD_CHAR_ERROR -> {
                            binding.signUpPasswordEt.error = getString(R.string.password_char_error)
                            validPasswordFlag = false
                            enableSignUpButtonIfAllValid(
                                validUsernameFlag,
                                validEmailFlag,
                                validPasswordFlag,
                                validConfirmPasswordFlag
                            )
                        }
                        ValidationErrorResults.PASSWORDS_NOT_MATCHED -> {
                            binding.signUpPassAgainEt.error = getString(R.string.password_match_error)
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
                    when (validationResult.result) {
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
                    UIHelperFunctions.showSnackbar(
                        binding.root,
                        requireContext(),
                        false,
                        it.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is EventState.Loading -> {
                    /** NO-OP **/
                }
                is EventState.Success -> {
                    UIHelperFunctions.showSnackbar(
                        binding.root,
                        requireContext(),
                        true,
                        it.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    navigateToSignInFragment()
                }
            }
        }
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = UIHelperFunctions.showSnackbar(
            binding.root,
            requireContext(),
            false,
            getString(R.string.no_internet_error),
            Snackbar.LENGTH_INDEFINITE
        )
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