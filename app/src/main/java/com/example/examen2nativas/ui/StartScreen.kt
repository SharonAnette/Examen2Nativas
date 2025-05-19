package com.example.examen2nativas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(
    onUserRegisterClick: () -> Unit,
    onAdminRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenida", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onUserRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse como Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAdminRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse como Administradora")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }
    }
}
