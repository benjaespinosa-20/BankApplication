package com.example.bankapplication.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import com.example.bankapplication.data.model.Movements
import com.example.bankapplication.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class AuthDataSource {
    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun signUp(email: String, password: String, userName: String, lastName: String): FirebaseUser? {
        val user = User(
            userEmail = email,
            userName = userName,
            userLastName = lastName,
            userBalance = (10000..100000).random().toString(),
            movements = movementsRandom()
        )
        val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        authResult.user?.uid?.let{uid ->
            FirebaseFirestore.getInstance().collection("users").document(uid).set(user).await()
        }
        return authResult.user
    }

    suspend fun updateUserProfile(imageBitmap: Bitmap, userName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/profile_picture")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl = imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .setPhotoUri(Uri.parse(downloadUrl))
            .build()
        user?.updateProfile(profileUpdates)?.await()
    }

    fun movementsRandom(): List<Movements> {
        val movements = mutableListOf<Movements>()
        repeat(15) {
            movements.add(
                Movements(
                    dateMovement = Timestamp.now(),
                    nameMovement = nameMovementRandom(),
                    paymentMovement = (100..10000).random().toString(),
                    imageMovement = when(nameMovementRandom()) {
                        "Deposit" ->  "https://static.vecteezy.com/system/resources/previews/005/879/972/non_2x/bank-deposit-icon-free-vector.jpg"
                        "Withdrawal" ->  "https://cdn-icons-png.flaticon.com/512/5024/5024665.png"
                        "Transfer" ->  "https://static.vecteezy.com/system/resources/previews/000/290/969/original/transaction-vector-icon.jpg"
                        "Payment" ->  "https://cdn2.iconfinder.com/data/icons/finance-336/3500/Loan-01-1024.png"
                        "Purchase" ->  "https://static.vecteezy.com/system/resources/previews/000/357/243/original/vector-buy-icon.jpg"
                        else -> ""
                    },
                    approveMovement = listOf(true, false).random()
                )
            )
        }
        return movements
    }

    fun nameMovementRandom(): String {
        val names = listOf("Deposit", "Withdrawal", "Transfer", "Payment", "Purchase")
        return names.random()
    }
}