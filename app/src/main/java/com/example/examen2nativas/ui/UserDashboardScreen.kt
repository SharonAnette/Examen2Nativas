package com.example.examen2nativas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.examen2nativas.R
import com.example.examen2nativas.model.User

@Composable
fun UserDashboardScreen(
    user: User,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    onViewNotifications: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título primero
        Text("Bienvenido, ${user.email}", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Información
        Text("Correo: ${user.name}")
        Text("Rol: ${user.role}")

        Spacer(modifier = Modifier.height(32.dp))

        // Acciones
        Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Editar perfil")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onViewNotifications, modifier = Modifier.fillMaxWidth()) {
            Text("Ver historial de notificaciones")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión")
        }
    }
}
