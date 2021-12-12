package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentSettingsBinding
import com.ferechamitbeyli.presentation.databinding.LogoutDialogBinding
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.helpers.PermissionManager
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.enable
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.hideKeyboard
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.showSnackbar
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.view.activities.auth_activity.AuthActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.SettingsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels()

    private var passwordChangedFlag: Boolean = false
    private var validPasswordFlag: Boolean = false
    private var validConfirmPasswordFlag: Boolean = false
    private var validWeightFlag: Boolean = true

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavigationViewVisibility()

        checkInternetConnection()

        populateFields()

        setupNotificationStateSwitch()

        setupOnClickListeners()

        addTextChangeListenersToPasswordFields()

    }

    private fun setupOnClickListeners() {
        binding.settingsSaveBtn.setOnClickListener {
            saveAllFieldsToRemoteDB()
        }

        binding.settingsLogoutBtn.setOnClickListener {
            showLogoutDialog()
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.add_run_fab).setOnClickListener {
            checkPermissionsBeforeNavigateToTrackingFragment()
        }
    }

    private fun navigateToLocationPermissionFragment() {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(R.id.action_settingsFragment_to_locationPermissionFragment)
        }
    }

    private fun checkPermissionsBeforeNavigateToTrackingFragment() {
        if (PermissionManager.hasLocationPermission(requireContext())) {
            navigateToTrackingFragment()
        } else {
            navigateToLocationPermissionFragment()
        }
    }

    private fun populateFields() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            combine(
                viewModel.getUsernameFromCache(),
                viewModel.getUserEmailFromCache(),
                viewModel.getUserWeightFromCache(),
                viewModel.getUserNotificationStateFromCache()
            ) { username, email, weight, notificationState ->

                binding.settingsUsernameEt.hint = username
                binding.settingsEmailEt.hint = email
                binding.settingsWeightEt.setText(weight.toString())

                binding.notificationStateSwitch.isChecked = notificationState
                binding.notificationStateTv.text = getString(R.string.notifications_enabled)
                if (notificationState) {
                    binding.notificationStateTv.text = getString(R.string.notifications_enabled)
                } else {
                    binding.notificationStateTv.text = getString(R.string.notifications_disabled)
                }

            }.collect()
        }

    private fun showLogoutDialog() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            val customDialog = Dialog(requireContext())

            customDialog.window?.apply {
                setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                setGravity(Gravity.CENTER)
                setBackgroundDrawable(getBlurBackgroundDrawable())
                attributes?.windowAnimations = android.R.style.Animation_Activity
            }

            customDialog.setCancelable(false)

            val bindingLogout: LogoutDialogBinding = LogoutDialogBinding.inflate(layoutInflater)
            customDialog.setContentView(bindingLogout.root)

            bindingLogout.logoutConfirmBtn.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.logout()
                    viewModel.resetCachedUser()
                    navigateToAuthActivity()
                    customDialog.dismiss()
                }
            }

            bindingLogout.logoutCancelBtn.setOnClickListener {
                customDialog.dismiss()
            }

            customDialog.show()
        }

    private fun checkIfWeightFieldIsValid(): Boolean {
        return binding.settingsWeightEt.text.toString().trim().isNotBlank()
    }

    private fun checkIfPasswordFieldsArValid(): Boolean {
        return binding.settingsPasswordEt.text.toString().trim()
            .isNotBlank() and binding.settingsPassAgainEt.text.toString().trim().isNotBlank()
    }

    private fun enableSaveButtonIfAllValid(
        passwordChanged: Boolean,
        passwordFlag: Boolean,
        passwordAgainFlag: Boolean,
        weightFlag: Boolean
    ) {
        if (passwordChanged) {
            if (passwordFlag and passwordAgainFlag and weightFlag) {
                binding.settingsSaveBtn.enable(true)
            } else {
                binding.settingsSaveBtn.enable(false)
            }
        } else {
            if (weightFlag) {
                binding.settingsSaveBtn.enable(true)
            } else {
                binding.settingsSaveBtn.enable(false)
            }
        }
    }

    private fun resetPasswordFields() {
        binding.settingsPasswordEt.text = null
        binding.settingsPasswordEt.clearFocus()
        binding.settingsPasswordEt.setError(null, null)
        binding.settingsPassAgainEt.text = null
        binding.settingsPassAgainEt.clearFocus()
        binding.settingsPassAgainEt.setError(null, null)
        passwordChangedFlag = checkIfPasswordFieldsArValid()
    }

    private fun addTextChangeListenersToPasswordFields() {

        binding.settingsPasswordEt.addTextChangedListener {
            passwordChangedFlag = checkIfPasswordFieldsArValid()
            if (passwordChangedFlag) {
                viewModel.validatePasswordsBeforeUpdating(
                    binding.settingsPasswordEt.text.toString(),
                    binding.settingsPassAgainEt.text.toString()
                )
                listenValidationChannel()
            }
        }

        binding.settingsPassAgainEt.addTextChangedListener {
            passwordChangedFlag = checkIfPasswordFieldsArValid()
            if (passwordChangedFlag) {
                viewModel.validatePasswordsBeforeUpdating(
                    binding.settingsPasswordEt.text.toString(),
                    binding.settingsPassAgainEt.text.toString()
                )
                listenValidationChannel()
            }
        }

        binding.settingsWeightEt.addTextChangedListener {
            validWeightFlag = checkIfWeightFieldIsValid()
        }
    }

    private fun setupNotificationStateSwitch() {
        binding.notificationStateSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.notificationStateTv.text = getString(R.string.notifications_enabled)
                setupPushNotification(true)
            } else {
                binding.notificationStateTv.text = getString(R.string.notifications_disabled)
                setupPushNotification(false)
            }
        }
    }

    private fun setupPushNotification(isEnabled: Boolean) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            when(isEnabled) {
                true -> FirebaseMessaging.getInstance().subscribeToTopic("APP").await()
                false -> FirebaseMessaging.getInstance().unsubscribeFromTopic("APP").await()
            }
        }
    }

    private fun navigateToAuthActivity() {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            requireActivity().startNewActivity(AuthActivity::class.java)
        }
    }

    private fun navigateToTrackingFragment() {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(R.id.action_settingsFragment_to_trackingFragment)
        }
    }

    private fun saveAllFieldsToRemoteDB() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            if (!passwordChangedFlag) {
                if (validWeightFlag and internetConnectionFlag) {
                    viewModel.saveAllFieldsOtherThanPasswordToRemoteDB(
                        binding.notificationStateSwitch.isChecked,
                        binding.settingsWeightEt.text.toString().toDouble()
                    )
                }
            } else {
                if (validPasswordFlag and validConfirmPasswordFlag and validWeightFlag and internetConnectionFlag) {
                    viewModel.savePasswordToUserProfile(
                        binding.settingsPasswordEt.text.toString()
                    ).also {
                        viewModel.saveAllFieldsOtherThanPasswordToRemoteDB(
                            binding.notificationStateSwitch.isChecked,
                            binding.settingsWeightEt.text.toString().toDouble()
                        )
                    }
                }
            }
            listenEventChannel()
            resetPasswordFields()
            hideKeyboard()
        }

    private fun listenEventChannel() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.settingsEventsChannel.collectLatest {
            when (it) {
                is EventState.Error -> {
                    showSnackbar(
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
                    showSnackbar(
                        binding.root,
                        requireContext(),
                        true,
                        it.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun listenValidationChannel() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        if (passwordChangedFlag) {
            viewModel.settingsValidationEventsChannel.collectLatest { validationResponse ->
                when (validationResponse) {
                    is ValidationState.ValidationError -> {
                        when (validationResponse.result) {
                            ValidationErrorResults.EMPTY_PASSWORD -> {
                                binding.settingsPasswordEt.error = "Please enter your password."
                                validPasswordFlag = false
                                enableSaveButtonIfAllValid(
                                    passwordChangedFlag,
                                    validPasswordFlag,
                                    validConfirmPasswordFlag,
                                    validWeightFlag,
                                )
                            }
                            ValidationErrorResults.EMPTY_CONFIRM_PASSWORD -> {
                                binding.settingsPassAgainEt.error = "Please enter your password."
                                validConfirmPasswordFlag = false
                                enableSaveButtonIfAllValid(
                                    passwordChangedFlag,
                                    validPasswordFlag,
                                    validConfirmPasswordFlag,
                                    validWeightFlag,
                                )
                            }
                            ValidationErrorResults.PASSWORD_CHAR_ERROR -> {
                                binding.settingsPasswordEt.error =
                                    "Your password must be longer than 6 character and must contain at least one capital, one lower letter, and one special character."
                                validPasswordFlag = false
                                enableSaveButtonIfAllValid(
                                    passwordChangedFlag,
                                    validPasswordFlag,
                                    validConfirmPasswordFlag,
                                    validWeightFlag,
                                )
                            }
                            ValidationErrorResults.PASSWORDS_NOT_MATCHED -> {
                                binding.settingsPassAgainEt.error = "The passwords are not matched."
                                validConfirmPasswordFlag = false
                                enableSaveButtonIfAllValid(
                                    passwordChangedFlag,
                                    validPasswordFlag,
                                    validConfirmPasswordFlag,
                                    validWeightFlag,
                                )
                            }
                            else -> {
                                /** NO-OP **/
                            }
                        }
                    }
                    is ValidationState.ValidationSuccess -> {
                        when (validationResponse.result) {
                            ValidationResults.VALID_BOTH_PASSWORDS -> {
                                println("BURADA_GIRDI $validConfirmPasswordFlag ve $validPasswordFlag")
                                validConfirmPasswordFlag = true
                                validPasswordFlag = true
                                println("BURADA_CIKTI $validConfirmPasswordFlag ve $validPasswordFlag")
                                enableSaveButtonIfAllValid(
                                    passwordChangedFlag,
                                    validPasswordFlag,
                                    validConfirmPasswordFlag,
                                    validWeightFlag,
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
    }

    private fun checkInternetConnection() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        val snackBar = showSnackbar(
            binding.root,
            requireContext(),
            false,
            "No Internet Connection",
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

    override fun onResume() {
        super.onResume()
        resetPasswordFields()
    }

    override fun onStop() {
        super.onStop()
        resetPasswordFields()
    }


}