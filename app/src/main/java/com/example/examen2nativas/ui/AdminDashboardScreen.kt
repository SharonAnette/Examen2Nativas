package com.example.examen2nativas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.examen2nativas.R
import com.example.examen2nativas.model.User

@Composable
fun AdminDashboardScreen(user: User, onLogout: () -> Unit) {
    var screen by remember { mutableStateOf("menu") }

    when (screen) {
        "menu" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text("Administrador: ${user.email}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                // Imagen de perfil
                Image(
                    painter = painterResource(id = R.drawable.default_avatar),
                    contentDescription = "Avatar admin",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )

                Text("Correo: ${user.name}")
                Text("Rol: ${user.role}")

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { screen = "editProfile" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar perfil")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { screen = "notificaciones" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar notificaciones")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { screen = "historial" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver historial de notificaciones")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar sesión")
                }
            }
        }

        "notificaciones" -> NotificationSenderScreen(
            currentUser = user,
            onBack = { screen = "menu" }
        )

        "historial" -> NotificationHistoryScreen(
            currentUserId = user.uid,
            onBack = { screen = "menu" }
        )

        "editProfile" -> EditProfileScreen(
            user = user,
            onBack = { screen = "menu" }
        )
    }
}
