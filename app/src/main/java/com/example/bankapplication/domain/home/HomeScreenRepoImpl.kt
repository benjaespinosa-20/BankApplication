package com.example.bankapplication.domain.home

import com.example.bankapplication.core.Result
import com.example.bankapplication.data.model.Movements
import com.example.bankapplication.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImpl(
    private val dataSource: HomeScreenDataSource
): HomeScreenRepo {
    override suspend fun getMovements(userId: String): Result<List<Movements>> = dataSource.getMovements(userId)
}