package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.presentation.R
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

        setupBottomNavigationViewVisibility()

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.startRunBtn.setOnClickListener {

        }

        binding.pauseRunBtn.setOnClickListener {

        }

        binding.finishRunBtn.setOnClickListener {
            showCancelRunDialog()
        }

        binding.goBackBtn.setOnClickListener {
            navigateToRunsFragment()
        }
    }

    private fun navigateToRunsFragment() {
        if (findNavController().currentDestination?.id == R.id.trackingFragment) {
            findNavController().navigate(R.id.action_trackingFragment_to_runsFragment)
        }
    }

    private fun showCancelRunDialog() =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val customDialog = Dialog(requireContext())
            customDialog.window?.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            customDialog.window?.setGravity(Gravity.CENTER)
            customDialog.window?.setBackgroundDrawable(getBlurBackgroundDrawable())
            customDialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog

            customDialog.setCancelable(false)

            val bindingCancelRun: CancelRunDialogBinding =
                CancelRunDialogBinding.inflate(layoutInflater)
            customDialog.setContentView(bindingCancelRun.root)

            bindingCancelRun.cancelRunConfirmBtn.setOnClickListener {
                /** CANCEL THE RUN HERE **/
                navigateToRunsFragment()
            }

            bindingCancelRun.cancelRunAbortBtn.setOnClickListener {
                customDialog.dismiss()
            }

            customDialog.show()
        }


}