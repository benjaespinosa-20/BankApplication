package com.example.bankapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.bankapplication.core.Result
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.bankapplication.R
import com.example.bankapplication.data.remote.home.HomeScreenDataSource
import com.example.bankapplication.databinding.FragmentHomeScreenBinding
import com.example.bankapplication.domain.home.HomeScreenRepoImpl
import com.example.bankapplication.presentation.home.HomeScreenViewModel
import com.example.bankapplication.presentation.home.HomeScreenViewModelFactory
import com.example.bankapplication.ui.home.adapter.HomeScreenAdapter
import com.google.firebase.auth.FirebaseAuth

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepoImpl(
                HomeScreenDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        binding.txtus.text = auth.currentUser?.displayName

        if (userId != null) {
            viewModel.fetchMovements(userId).observe(viewLifecycleOwner) { result ->
                when (result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvHome.adapter = HomeScreenAdapter(result.data)
                    }

                    is Result.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "ocurrio un error", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }


    }


}