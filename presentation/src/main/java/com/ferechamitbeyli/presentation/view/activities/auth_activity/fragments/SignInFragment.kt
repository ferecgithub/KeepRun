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
import com.ferechamitbeyli.presentation.databinding.FragmentSignInBinding
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.view.activities.home_activity.HomeActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    private var validEmailFlag: Boolean = false
    private var validPasswordFlag: Boolean = false
    private var internetConnectionFlag: Boolean = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignInBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInternetConnection()

        // Disable Sign In Button for Validation
        enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)

        addTextChangeListenersToFields()

        binding.signInBtn.setOnClickListener {
            if (validEmailFlag and validPasswordFlag and internetConnectionFlag) {
                viewModel.signInUser(
                    binding.signInEmailEt.text.toString(),
                    binding.signInPasswordEt.text.toString()
                )
            }
            listenEventChannel()
            hideKeyboard()
        }

        binding.forgotPassTv.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.signInFragment) {
                findNavController().navigate(R.id.action_signInFragment_to_forgotPassFragment)
            }
        }

        binding.signUpTv.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.signInFragment) {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }

    }

    private fun enableSignInButtonIfAllValid(emailFlag: Boolean, passwordFlag: Boolean) {
        if (emailFlag and passwordFlag) {
            binding.signInBtn.enable(true)
        } else {
            binding.signInBtn.enable(false)
        }
    }

    private fun addTextChangeListenersToFields() {
        binding.signInEmailEt.addTextChangedListener {
            viewModel.validateBeforeSignIn(
                binding.signInEmailEt.text.toString(),
                binding.signInPasswordEt.text.toString()
            )
            listenValidationChannel()
        }

        binding.signInPasswordEt.addTextChangedListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                delay(1000)
                viewModel.validateBeforeSignIn(
                    binding.signInEmailEt.text.toString(),
                    binding.signInPasswordEt.text.toString()
                )
                listenValidationChannel()
            }

        }
    }

    private fun listenValidationChannel() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.authValidationEventsChannel.collect {
            when (it) {
                is ValidationState.ValidationError -> {
                    when (it.result) {
                        ValidationErrorResults.EMPTY_EMAIL -> {
                            binding.signInEmailEt.error = "Please enter your email."
                            validEmailFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        ValidationErrorResults.INVALID_EMAIL -> {
                            binding.signInEmailEt.error = "Invalid email."
                            validEmailFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        ValidationErrorResults.EMPTY_PASSWORD -> {
                            binding.signInPasswordEt.error = "Please enter your password."
                            validPasswordFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        ValidationErrorResults.PASSWORD_CHAR_ERROR -> {
                            binding.signInPasswordEt.error = "Password configuration is wrong."
                            validPasswordFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
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
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        ValidationResults.VALID_PASSWORD -> {
                            validPasswordFlag = true
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
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
                }
                is EventState.Loading -> {
                    /** NO-OP **/
                }
                is EventState.Success -> {
                    requireActivity().startNewActivity(HomeActivity::class.java)
                }
            }
        }
    }


    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
        viewModel.networkState.collect {
            if (it) {
                internetConnectionFlag = true
                snackBar.dismiss()
            } else {
                internetConnectionFlag = false
                snackBar.show()
            }
        }
        /*
        viewModel.networkState.collect {

        }

         */
    }


}