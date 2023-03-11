package com.dum.effectivemobiletest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dum.effectivemobiletest.databinding.FragmentLoginBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        progressBar = binding.progressCircular

        binding.loginBtn.setOnClickListener {

            it.hideKeyboard()

            binding.loginBtn.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val inputEmail = binding.emailEditText.text.toString()
            val inputPassword = binding.passwordEditText.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                login(auth, inputEmail, inputPassword)
            }

            /* auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
             if (it.isSuccessful) {
                 findNavController().navigate(R.id.action_loginFragment_to_page1Fragment)
             } else {
                 Toast.makeText(
                     context,
                     "Please check your email, password and try again",
                     Toast.LENGTH_LONG
                 ).show()
             }
         }*/
        }
    }


    suspend fun login(
        firebaseAuth: FirebaseAuth,
        inputEmail: String,
        inputPassword: String
    ): AuthResult? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .await()
            updateUI(result.user)
            result
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("AuthResult", "${e.message}")
                progressBar.visibility = View.GONE
            }
            null
        }

    }

    private suspend fun updateUI(firebaseUser: FirebaseUser?) {
        Log.d("AuthResult", "${firebaseUser?.email}")
        withContext(Dispatchers.Main) {
            progressBar.visibility = View.GONE
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginFragment_to_page1Fragment)
        }
    }
}
