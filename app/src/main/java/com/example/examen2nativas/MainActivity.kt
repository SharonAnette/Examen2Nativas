package com.example.examen2nativas

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.examen2nativas.model.LoginScreen
import com.example.examen2nativas.model.User
import com.example.examen2nativas.ui.*
import com.example.examen2nativas.ui.theme.Examen2NativasTheme
import com.example.examen2nativas.utils.FirebaseUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permiso para notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        setContent {
            Examen2NativasTheme {
                var screen by remember { mutableStateOf("start") }
                var currentUser by remember { mutableStateOf<User?>(null) }

                when (screen) {
                    "start" -> StartScreen(
                        onUserRegisterClick = { screen = "registerUser" },
                        onAdminRegisterClick = { screen = "registerAdmin" },
                        onLoginClick = { screen = "login" }
                    )

                    "registerUser" -> RegisterScreen(
                        onRegisterSuccess = { screen = "login" },
                        onBack = { screen = "start" }
                    )

                    "registerAdmin" -> AdminRegisterScreen(
                        onRegisterSuccess = { screen = "login" },
                        onBack = { screen = "start" }
                    )

                    "login" -> LoginScreen(
                        onLoginSuccess = { user ->
                            currentUser = user
                            screen = if (user.role == "admin") "dashboardAdmin" else "dashboardUser"

                            // Guardar token FCM
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                                val uid = Firebase.auth.currentUser?.uid
                                if (uid != null) {
                                    Firebase.firestore.collection("users").document(uid)
                                        .update("fcmToken", token)
                                        .addOnSuccessListener {
                                            Log.d("FCM", "Token actualizado en Firestore")
                                        }
                                        .addOnFailureListener {
                                            Log.e("FCM", "Error al guardar token: ${it.message}")
                                        }
                                }
                            }
                        },
                        onBack = { screen = "start" }
                    )

                    "dashboardUser" -> currentUser?.let { user ->
                        UserDashboardScreen(
                            user = user,
                            onLogout = {
                                FirebaseUtil.auth.signOut()
                                currentUser = null
                                screen = "start"
                            },
                            onEditProfile = { screen = "editProfile" },
                            onViewNotifications = { screen = "userNotifications" } // ← Esto activa el historial
                        )
                    }

                    "dashboardAdmin" -> currentUser?.let { user ->
                        AdminDashboardScreen(user = user) {
                            FirebaseUtil.auth.signOut()
                            currentUser = null
                            screen = "start"
                        }
                    }

                    "editProfile" -> currentUser?.let { user ->
                        EditProfileScreen(user = user, onBack = { screen = "dashboardUser" })
                    }

                    "historial" -> currentUser?.let { user ->
                        NotificationHistoryScreen(
                            currentUserId = user.uid,
                            onBack = { screen = "menu" }
                        )
                    }

                    "userNotifications" -> currentUser?.let { user ->
                        NotificationHistoryScreen(
                            currentUserId = user.uid,
                            onBack = { screen = "dashboardUser" }
                        )
                    }
                }
            }
        }
    }

    private fun NotificationHistoryScreenFiltered(user: User, onBack: () -> Unit) {

    }
}
