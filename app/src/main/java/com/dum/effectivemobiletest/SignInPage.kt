package com.dum.effectivemobiletest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dum.effectivemobiletest.databinding.FragmentSignInPageBinding

class SignInPage : Fragment() {

    lateinit var binding: FragmentSignInPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}