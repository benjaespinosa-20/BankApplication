package com.example.bankapplication.data.model

import com.google.firebase.Timestamp

data class Movements(
    val dateMovement: Timestamp? = null,
    val nameMovement: String? = "",
    val paymentMovement: String? = "",
    val imageMovement: String? = null,
    val approveMovement: Boolean? = true
)