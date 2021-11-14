package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.ferechamitbeyli.presentation.databinding.FragmentSettingsBinding
import com.ferechamitbeyli.presentation.databinding.LogoutDialogBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun showLogoutDialog() {
        val customDialog = Dialog(requireContext())
        customDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
        customDialog.setCancelable(false)

        val bindingLogout : LogoutDialogBinding = LogoutDialogBinding.inflate(layoutInflater)
        customDialog.setContentView(bindingLogout.root)
        bindingLogout.logoutConfirmBtn.setOnClickListener {

        }
        bindingLogout.logoutCancelBtn.setOnClickListener {

        }
    }

}