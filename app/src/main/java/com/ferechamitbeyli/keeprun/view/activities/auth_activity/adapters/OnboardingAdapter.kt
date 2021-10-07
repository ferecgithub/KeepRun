package com.ferechamitbeyli.keeprun.view.activities.auth_activity.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(
        list: ArrayList<Fragment>,
        fm: FragmentManager,
        lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = list

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]


}
