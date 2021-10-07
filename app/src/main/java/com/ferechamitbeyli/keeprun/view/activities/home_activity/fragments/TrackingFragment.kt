package com.ferechamitbeyli.keeprun.view.activities.home_activity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferechamitbeyli.keeprun.R
import com.ferechamitbeyli.keeprun.databinding.FragmentTrackingBinding
import com.ferechamitbeyli.keeprun.view.base.BaseFragment

class TrackingFragment : BaseFragment<FragmentTrackingBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTrackingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}