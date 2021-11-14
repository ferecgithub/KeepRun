package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.ferechamitbeyli.presentation.databinding.CancelRunDialogBinding
import com.ferechamitbeyli.presentation.databinding.FragmentTrackingBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : BaseFragment<FragmentTrackingBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTrackingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun showCancelRunDialog() {
        val customDialog = Dialog(requireContext())
        customDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
        customDialog.setCancelable(false)

        val bindingCancelRun : CancelRunDialogBinding = CancelRunDialogBinding.inflate(layoutInflater)
        customDialog.setContentView(bindingCancelRun.root)
        bindingCancelRun.cancelRunConfirmBtn.setOnClickListener {

        }
        bindingCancelRun.cancelRunAbortBtn.setOnClickListener {

        }
    }



}