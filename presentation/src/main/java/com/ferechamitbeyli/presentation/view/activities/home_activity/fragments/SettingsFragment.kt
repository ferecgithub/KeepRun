package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.FragmentSettingsBinding
import com.ferechamitbeyli.presentation.databinding.LogoutDialogBinding
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.startNewActivity
import com.ferechamitbeyli.presentation.view.activities.auth_activity.AuthActivity
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateFields()
        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        binding.settingsSaveBtn.setOnClickListener {

        }

        binding.settingsLogoutBtn.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun navigateToAuthActivity() {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            requireActivity().startNewActivity(AuthActivity::class.java)
        }
    }

    private fun populateFields() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUsernameFromCache().collectLatest {
                println("SETTINGS_FR_USERNAME : $it")
                binding.settingsUsernameEt.hint = it
            }
            viewModel.getUserEmailFromCache().collectLatest {
                println("SETTINGS_FR_EMAIL : $it")
                binding.settingsEmailEt.hint = it
            }
            viewModel.getUserWeightFromCache().collectLatest {
                println("SETTINGS_FR_WEIGHT : $it")
                binding.settingsWeightEt.hint = it.toString()
            }
            viewModel.getUserNotificationStateFromCache().collectLatest {
                println("SETTINGS_FR_NOTIFICATION : $it")
                binding.notificationStateSwitch.isChecked = it
                binding.notificationStateTv.text = getString(R.string.notifications_enabled)
                if (it) {
                    binding.notificationStateTv.text = getString(R.string.notifications_enabled)
                } else {
                    binding.notificationStateTv.text = getString(R.string.notifications_disabled)
                }
            }
        }

    private fun showLogoutDialog() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val customDialog = Dialog(requireContext())

            customDialog.window?.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
            customDialog.window?.setGravity(Gravity.CENTER)
            customDialog.window?.setBackgroundDrawable(getBlurBackgroundDrawable())
            customDialog.window?.attributes?.windowAnimations = android.R.style.Animation_Activity

            customDialog.setCancelable(false)

            val bindingLogout: LogoutDialogBinding = LogoutDialogBinding.inflate(layoutInflater)
            customDialog.setContentView(bindingLogout.root)

            bindingLogout.logoutConfirmBtn.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.logout()
                    viewModel.resetCachedUser()
                    navigateToAuthActivity()
                }
            }

            bindingLogout.logoutCancelBtn.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }


}