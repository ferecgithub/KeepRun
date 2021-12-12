package com.ferechamitbeyli.presentation.view.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.BlurTransformation
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.async

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    var internetConnectionFlag: Boolean = false

    private var _binding: B? = null
    val binding get() = _binding!!

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater, container)

        return binding.root
    }

    protected fun setSpannableTextColor(
        view: TextView,
        fulltext: String,
        subtext: String,
        color: Int
    ) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE)
        val str = view.text as Spannable
        val i = fulltext.indexOf(subtext)
        str.setSpan(
            ForegroundColorSpan(color),
            i,
            i + subtext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun setupBottomNavigationViewVisibility() {

        when (findNavController().currentDestination?.id) {
            R.id.initialFragment -> {
                hideBottomNavigationView()
            }
            R.id.trackingFragment -> {
                hideBottomNavigationView()
            }
            R.id.locationPermissionFragment -> {
                hideBottomNavigationView()
            }
            R.id.activityRecognitionPermissionFragment -> {
                hideBottomNavigationView()
            }
            else -> {
                showBottomNavigationView()
            }
        }
    }

    private fun hideBottomNavigationView() {
        requireActivity().findViewById<BottomNavigationView>(R.id.home_bab).visible(false)
        requireActivity().findViewById<BottomNavigationView>(R.id.home_bnv).visible(false)
        requireActivity().findViewById<BottomNavigationView>(R.id.add_run_fab).visible(false)
    }

    private fun showBottomNavigationView() {
        requireActivity().findViewById<BottomNavigationView>(R.id.home_bab).visible(true)
        requireActivity().findViewById<BottomNavigationView>(R.id.home_bnv).visible(true)
        requireActivity().findViewById<BottomNavigationView>(R.id.add_run_fab).visible(true)
    }

    suspend fun getBlurBackgroundDrawable(): Drawable =
        viewLifecycleOwner.lifecycleScope.async {
            val imageLoader = ImageLoader.Builder(requireContext())
                .build()
            val request = ImageRequest.Builder(requireContext())
                .data(UIHelperFunctions.getScreenShot(requireActivity().findViewById(R.id.homeActivity_layout)))
                .transformations(BlurTransformation(requireContext(), 15f))
                .build()
            return@async (imageLoader.execute(request) as SuccessResult).drawable
        }.await()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}