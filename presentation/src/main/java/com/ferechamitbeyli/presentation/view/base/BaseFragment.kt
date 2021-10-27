package com.ferechamitbeyli.presentation.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import javax.inject.Inject

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected lateinit var binding: B

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = getFragmentBinding(inflater, container)

        return binding.root
    }

}