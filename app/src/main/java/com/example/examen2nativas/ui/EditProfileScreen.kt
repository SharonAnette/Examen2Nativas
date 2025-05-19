package com.example.examen2nativas.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.examen2nativas.model.User
import com.example.examen2nativas.utils.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun EditProfileScreen(user: User, onBack: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(user.name) }
    var photoUrl by remember { mutableStateOf(user.photoUrl ?: "") }
    var isSaving by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("profile_photos/${user.uid}_${UUID.randomUUID()}.jpg")
            imageRef.putFile(it)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        photoUrl = downloadUri.toString()
                        Toast.makeText(context, "Foto actualizada", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al subir la foto", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar Perfil", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(photoUrl.ifEmpty { "https://via.placeholder.com/150" }),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .clickable { launcher.launch("image/*") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isSaving = true
                val updatedUser = user.copy(name = name, photoUrl = photoUrl)
                FirebaseFirestore.getInstance().collection("users")
                    .document(user.uid)
                    .set(updatedUser)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        isSaving = false
                    }
            },
            enabled = !isSaving
        ) {
            Text(if (isSaving) "Guardando..." else "Guardar cambios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onBack) {
            Text("Volver")
        }
    }
}

fun rememberAsyncImagePainter(ifEmpty: String): Painter {
    TODO("Not yet implemented")
}
