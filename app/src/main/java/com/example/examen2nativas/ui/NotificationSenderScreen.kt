package com.example.examen2nativas.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.examen2nativas.model.User
import com.example.examen2nativas.utils.FirebaseUtil
import com.google.firebase.functions.FirebaseFunctions

@Composable
fun NotificationSenderScreen(currentUser: User, onBack: () -> Unit) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(listOf<User>()) }
    var selectedUsers by remember { mutableStateOf(setOf<User>()) }

    val functions = FirebaseFunctions.getInstance()

    // Cargar usuarios desde Firestore
    LaunchedEffect(Unit) {
        FirebaseUtil.db.collection("users").get()
            .addOnSuccessListener { result ->
                users = result.documents.mapNotNull { it.toObject(User::class.java) }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("←", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Enviar Notificación", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Selecciona uno o varios destinatarios:")

        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedUsers.contains(user),
                        onCheckedChange = {
                            selectedUsers = if (it) selectedUsers + user else selectedUsers - user
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${user.name} (${user.email})")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val tokens = selectedUsers.mapNotNull { it.fcmToken }.filter { it.isNotEmpty() }

                if (tokens.isEmpty()) {
                    Toast.makeText(context, "Selecciona al menos un usuario válido con token FCM", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                tokens.forEach { token ->
                    val data = hashMapOf(
                        "token" to token,
                        "title" to title,
                        "body" to body
                    )

                    functions
                        .getHttpsCallable("sendNotification")
                        .call(data)
                }

                Toast.makeText(context, "Notificación enviada a ${tokens.size} usuario(s)", Toast.LENGTH_SHORT).show()
                title = ""
                body = ""
                selectedUsers = emptySet()
            },
            enabled = selectedUsers.isNotEmpty() && title.isNotEmpty() && body.isNotEmpty()
        ) {
            Text("Enviar Notificación")
        }
    }
}
