package com.example.examen2nativas.fcm

import android.util.Log
import com.example.examen2nativas.utils.FirebaseUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val uid = FirebaseUtil.auth.currentUser?.uid
        if (uid != null) {
            FirebaseUtil.db.collection("users")
                .document(uid)
                .update("fcmToken", token)
                .addOnSuccessListener {
                    Log.d("FCM", "Token actualizado en Firestore")
                }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", "Mensaje recibido: ${message.notification?.title} - ${message.notification?.body}")
    }
}
