package com.example.bankapplication.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bankapplication.core.Result
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bankapplication.R
import com.example.bankapplication.data.remote.auth.AuthDataSource
import com.example.bankapplication.databinding.FragmentRegisterBinding
import com.example.bankapplication.domain.auth.AuthRepoImpl
import com.example.bankapplication.presentation.auth.AuthViewModel
import com.example.bankapplication.presentation.auth.AuthViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        signUp()

    }

    private fun signUp() {

        binding.btnSignUp.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val userName = binding.editTextUsername.text.toString().trim()
            val userLastName = binding.editTextUserLastName.text.toString().trim()

            if (password != confirmPassword) {
                binding.editTextConfirmPassword.error = "Password does not match"
                binding.editTextPassword.error = "Password does not match"
                return@setOnClickListener
            }

            if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createUser(email, password, userName, userLastName)

        }
    }

    private fun createUser(email: String, password: String, userName: String, lastName: String) {
        viewModel.signUp(email, password, userName, lastName).observe(viewLifecycleOwner) {result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignUp.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.VISIBLE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignUp.isEnabled = true
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

}