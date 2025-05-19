package com.example.examen2nativas.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "normal",
    val fcmToken: String? = null, // FCM
    val photoUrl: String? = null
)