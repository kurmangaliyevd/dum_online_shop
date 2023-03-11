package com.dum.effectivemobiletest

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dum.effectivemobiletest.databinding.FragmentSignUpPageBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpPageFragment : Fragment() {

    lateinit var binding: FragmentSignUpPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        progressBar = binding.progressCircular

        binding.logIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpPageFragment_to_loginFragment)
        }
        binding.signUpBtn.setOnClickListener {
            it.hideKeyboard()

            val isFirstNameOkay = binding.firstName.text.toString().length.let { it >= 2 }
            val isLastNameOkay = binding.lastName.text.toString().length.let { it >= 2 }
            val isEmailOkay = binding.emailEditText.text.toString().isValidEmail()
            val isPassOkay =
                binding.passwordEditText.text?.toString()?.length?.let { it >= 8 } ?: false

            val inputEmail = binding.emailEditText.text.toString()
            val inputPassword = binding.passwordEditText.text.toString()

            if (!isFirstNameOkay) {
                binding.firstName.error = "Your first name must be more than 2 characters long"
            } else if (!isLastNameOkay) {
                binding.lastName.error = "Your last name must be more than 2 characters long"
            } else if (!isEmailOkay) {
                binding.emailEditText.error = "Email address is not valid"
            } else if (!isPassOkay) {
                binding.passwordEditText.error = "Your password must be more than 8 characters long"
            } else {
                binding.emailEditText.error = null
                binding.passwordEditText.error = null

                progressBar.visibility = View.VISIBLE

                lifecycleScope.launch(Dispatchers.IO) {
                    signUp(auth, inputEmail, inputPassword)
                }

                /*auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController().navigate(R.id.action_signUpPageFragment_to_page1Fragment)
                        } else {
                            Toast.makeText(context, "You've already registered. Please log in", Toast.LENGTH_LONG).show()
                        }
                    }*/
            }
        }
    }

    suspend fun signUp(
        firebaseAuth: FirebaseAuth,
        inputEmail: String,
        inputPassword: String
    ): AuthResult? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .await()
            updateUI(result.user)
            result
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("test title")
                    setMessage("test message")
                    setPositiveButton(
                        "yes"
                    ) { dialog, _ -> dialog.dismiss() }
                    show()
                }
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
            findNavController().navigate(R.id.action_signUpPageFragment_to_page1Fragment)
        }
    }
}


