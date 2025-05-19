package com.example.examen2nativas.ui

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val MASTER_PASSWORD = "adminSecret123"

@Composable
fun AdminRegisterScreen(onRegisterSuccess: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var masterPassword by remember { mutableStateOf("") }
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
        Text("Registro de Administrador", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = masterPassword,
            onValueChange = { masterPassword = it },
            label = { Text("Contraseña maestra") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (masterPassword == MASTER_PASSWORD) {
                    isLoading = true
                    FirebaseUtil.auth.createUserWithEmailAndPassword(email, "admin123")
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: ""
                            val user = User(uid, name, email, "admin", "")
                            Firebase.firestore.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    isLoading = false
                                    Toast.makeText(context, "Administrador registrado", Toast.LENGTH_SHORT).show()
                                    onRegisterSuccess()
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                    Toast.makeText(context, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Contraseña maestra incorrecta", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Registrando..." else "Registrar")
        }
    }
}
