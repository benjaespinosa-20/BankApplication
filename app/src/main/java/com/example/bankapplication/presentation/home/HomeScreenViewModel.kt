package com.example.bankapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.bankapplication.core.Result
import com.example.bankapplication.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(private val repo: HomeScreenRepo): ViewModel() {

    fun fetchMovements(userId: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(repo.getMovements(userId))
        }catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }

}