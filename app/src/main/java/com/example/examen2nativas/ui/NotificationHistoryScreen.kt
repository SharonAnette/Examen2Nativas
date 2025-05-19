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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

data class NotificationRecord(
    val title: String = "",
    val body: String = "",
    val timestamp: Long? = null,
    val toUserId: String = ""
)


@Composable
fun NotificationHistoryScreen(
    currentUserId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var notifications by remember { mutableStateOf(listOf<Pair<String, NotificationRecord>>()) }

    fun loadNotifications() {
        db.collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                notifications = result.documents.mapNotNull { doc ->
                    try {
                        val title = doc.getString("title") ?: ""
                        val body = doc.getString("body") ?: ""
                        val toUserId = doc.getString("toUserId") ?: ""
                        val timestamp = doc.getTimestamp("timestamp")?.toDate()?.time
                        val record = NotificationRecord(title, body, timestamp, toUserId)
                        Pair(doc.id, record)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    LaunchedEffect(Unit) {
        loadNotifications()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Flechita de regreso
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("←", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Historial de Notificaciones", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de borrar arriba
        Button(
            onClick = {
                db.collection("notifications")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val batch = db.batch()
                        for (doc in snapshot.documents) {
                            batch.delete(doc.reference)
                        }
                        batch.commit().addOnSuccessListener {
                            Toast.makeText(context, "Historial eliminado correctamente", Toast.LENGTH_SHORT).show()
                            notifications = emptyList()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Error al borrar historial", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("🗑️ Borrar historial", color = MaterialTheme.colorScheme.onError)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(notifications) { (_, notification) ->
                val dateFormatted = notification.timestamp?.let {
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
                } ?: "Fecha no disponible"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Título: ${notification.title}", style = MaterialTheme.typography.bodyLarge)
                        Text("Mensaje: ${notification.body}", style = MaterialTheme.typography.bodyMedium)
                        Text("UID destinatario: ${notification.toUserId}", style = MaterialTheme.typography.labelSmall)
                        Text("Fecha: $dateFormatted", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
