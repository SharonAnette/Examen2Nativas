package com.example.examen2nativas.model

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.examen2nativas.model.User
import com.example.examen2nativas.utils.FirebaseUtil
import com.google.firebase.firestore.ktx.toObject

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier
                .align(Alignment.Start)
        ) {
            Text("←", color = MaterialTheme.colorScheme.onPrimary)
        }


        Spacer(modifier = Modifier.height(32.dp))
        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                FirebaseUtil.auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: ""
                        FirebaseUtil.db.collection("users").document(uid)
                            .get()
                            .addOnSuccessListener { doc ->
                                val user = doc.toObject<User>()
                                if (user != null) {
                                    isLoading = false
                                    Toast.makeText(context, "Bienvenido ${user.name}", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(user)
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "No se encontró el usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                isLoading = false
                                Toast.makeText(context, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        isLoading = false
                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Entrando..." else "Iniciar sesión")
        }
    }
}
