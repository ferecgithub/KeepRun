package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferechamitbeyli.presentation.databinding.FragmentAboutBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment

class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding = FragmentAboutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}