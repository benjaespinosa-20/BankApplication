package com.example.bankapplication.domain.auth

import android.graphics.Bitmap
import com.example.bankapplication.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val dataSource: AuthDataSource): AuthRepo {
    override suspend fun signIn(email: String, password: String): FirebaseUser? = dataSource.signIn(email, password)
    override suspend fun signUp(email: String, password: String, userName: String, lastName: String): FirebaseUser? =
        dataSource.signUp(email, password, userName, lastName)

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) = dataSource.updateUserProfile(imageBitmap, username)
}