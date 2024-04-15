package com.example.bankapplication.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.example.bankapplication.core.Result
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bankapplication.R
import com.example.bankapplication.data.remote.auth.AuthDataSource
import com.example.bankapplication.databinding.FragmentSetupProfileBinding
import com.example.bankapplication.domain.auth.AuthRepoImpl
import com.example.bankapplication.presentation.auth.AuthViewModel
import com.example.bankapplication.presentation.auth.AuthViewModelFactory

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {
    private lateinit var binding: FragmentSetupProfileBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    private val REQUEST_IMAGE_CAPTURE = 1
    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)

        binding.btnOpenCamera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Not found Camera App", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCreateProfile.setOnClickListener {
            val userName = binding.etxtUsername.text.toString().trim()
            val alerDialog = AlertDialog.Builder(requireContext()).setTitle("Uploading photo...").create()
            bitmap?.let {
                viewModel.updateUserProfile(imageBitmap = it, userName).observe(viewLifecycleOwner){ result ->
                    when (result) {
                        is Result.Loading -> {
                            alerDialog.show()
                        }
                        is Result.Success -> {
                            alerDialog.dismiss()
                            //Toast.makeText(requireContext(), "Registration completed", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                        }
                        is Result.Failure -> {
                            alerDialog.dismiss()
                        }
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }
}