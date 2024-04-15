package com.example.bankapplication.domain.home

import com.example.bankapplication.core.Result
import com.example.bankapplication.data.model.Movements

interface HomeScreenRepo {
    suspend fun getMovements(userId: String): Result<List<Movements>>
}