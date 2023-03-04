package com.dum.effectivemobiletest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dum.effectivemobiletest.databinding.FragmentPage1Binding

class Page1Fragment : Fragment() {

    lateinit var binding: FragmentPage1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPage1Binding.inflate(layoutInflater)
        return binding.root
    }

}