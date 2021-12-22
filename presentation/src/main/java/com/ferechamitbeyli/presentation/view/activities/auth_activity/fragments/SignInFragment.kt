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
import com.ferechamitbeyli.presentation.databinding.FragmentSignInBinding
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.showSnackbar
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.view.activities.home_activity.HomeActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    private var validEmailFlag: Boolean = false
    private var validPasswordFlag: Boolean = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignInBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpannableTextColorOfSignUp()

        checkInternetConnection()

        // Disable Sign In Button for Validation
        enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)

        addTextChangeListenersToFields()

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.signInBtn.setOnClickListener {
            signInUser()
        }

        binding.forgotPassTv.setOnClickListener {
            navigateToForgotPassFragment()
        }

        binding.signInToSignUpTv.setOnClickListener {
            navigateToSignUpFragment()
        }
    }

    private fun signInUser() {
        if (validEmailFlag and validPasswordFlag and internetConnectionFlag) {
            viewModel.signInUser(
                binding.signInEmailEt.text.toString(),
                binding.signInPasswordEt.text.toString()
            )
        }
        listenEventChannel()
        hideKeyboard()
    }

    private fun navigateToForgotPassFragment() {
        if (findNavController().currentDestination?.id == R.id.signInFragment) {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPassFragment)
        }
    }

    private fun navigateToSignUpFragment() {
        if (findNavController().currentDestination?.id == R.id.signInFragment) {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun setSpannableTextColorOfSignUp() {
        setSpannableTextColor(
            binding.signInToSignUpTv,
            resources.getString(R.string.do_not_have_acc),
            resources.getString(R.string.sign_up),
            ContextCompat.getColor(requireContext(), R.color.darkGreen)
        )
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
            viewModel.validateBeforeSignIn(
                binding.signInEmailEt.text.toString(),
                binding.signInPasswordEt.text.toString()
            )
            listenValidationChannel()

        }
    }

    private fun listenValidationChannel() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.authValidationEventsChannel.collectLatest { validationResponse ->
            when (validationResponse) {
                is ValidationState.ValidationError -> {
                    when (validationResponse.result) {
                        ValidationErrorResults.EMPTY_EMAIL -> {
                            binding.signInEmailEt.error = getString(R.string.email_empty_error)
                            validEmailFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        ValidationErrorResults.INVALID_EMAIL -> {
                            binding.signInEmailEt.error = getString(R.string.email_invalid_error)
                            validEmailFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        ValidationErrorResults.EMPTY_PASSWORD -> {
                            binding.signInPasswordEt.error =
                                getString(R.string.password_empty_error)
                            validPasswordFlag = false
                            enableSignInButtonIfAllValid(validEmailFlag, validPasswordFlag)
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
                is ValidationState.ValidationSuccess -> {
                    when (validationResponse.result) {
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
        viewModel.authEventsChannel.collectLatest {
            when (it) {
                is EventState.Error -> {
                    showSnackbar(
                        binding.root,
                        requireContext(),
                        false,
                        it.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                    setAvailabilityOfSignInButton(true)
                }
                is EventState.Loading -> {
                    setAvailabilityOfSignInButton(false)
                }
                is EventState.Success -> {
                    setAvailabilityOfSignInButton(true)
                    requireActivity().startNewActivity(HomeActivity::class.java)
                }
            }
        }
    }

    private fun setAvailabilityOfSignInButton(isEnabled: Boolean) {
        binding.signInBtn.enable(isEnabled)
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = showSnackbar(
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
                setAvailabilityOfSignInButton(true)
            }
        }
    }


}