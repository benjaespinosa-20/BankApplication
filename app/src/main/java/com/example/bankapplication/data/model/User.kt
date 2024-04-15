package com.example.bankapplication.data.model

data class User(
    val userEmail: String = "",
    val userName: String = "",
    val userLastName: String = "",
    val userBalance: String = "",
    val movements: List<Movements> = listOf()
)