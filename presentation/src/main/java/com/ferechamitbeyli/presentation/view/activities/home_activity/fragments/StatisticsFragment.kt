package com.ferechamitbeyli.presentation.view.activities.home_activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferechamitbeyli.presentation.databinding.FragmentStatisticsBinding
import com.ferechamitbeyli.presentation.view.base.BaseFragment

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatisticsBinding = FragmentStatisticsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}