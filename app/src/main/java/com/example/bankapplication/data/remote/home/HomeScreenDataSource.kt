package com.example.bankapplication.data.remote.home

import com.example.bankapplication.core.Result
import com.example.bankapplication.data.model.Movements
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getMovements(userId: String): Result<List<Movements>> {
        val movementsList = mutableListOf<Movements>()
        val userIdFire = FirebaseFirestore.getInstance().collection("users").document(userId)
        val movementsByUserId = userIdFire.get().await().get("movements") as? List<HashMap<String,Any>>

        movementsByUserId?.let {
            for (movementData in it) {
                val movement = Movements(
                    dateMovement = movementData["dateMovement"] as? Timestamp,
                    nameMovement = movementData["nameMovement"] as? String,
                    paymentMovement = movementData["paymentMovement"] as? String,
                    imageMovement = movementData["imageMovement"] as? String,
                    approveMovement = movementData["approveMovement"] as? Boolean
                )
                movementsList.add(movement)
            }
        }
        return Result.Success(movementsList)
    }


}