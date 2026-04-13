package com.example.calmlist.presentation.Screens
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.calmlist.R
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel
import com.example.calmlist.util.playAudio

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.calmlist.util.ImageUtils
import com.example.calmlist.util.AudioRecorder

@Composable

fun WishDetailScreen(navController: NavHostController, wishViewModel: WishDetailViewModel) {
    val homeState by wishViewModel.HomeScreensate
    val wish by wishViewModel.selectedWish

    // Handle Navigation on Success
    LaunchedEffect(homeState.success) {
        if (homeState.success == true) {
            navController.popBackStack()
            wishViewModel.resetHomeScreenState()
        }
    }

    if (wish == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.serene_background)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colorResource(R.color.serene_primary))
        }
        return
    }

    // Local State for Editing
    var title by remember { mutableStateOf(wish!!.title ?: "") }
    var note by remember { mutableStateOf(wish!!.note ?: "") }
    var imagePath by remember { mutableStateOf(wish!!.imagePath) }
    var audioPath by remember { mutableStateOf(wish!!.audioPath) }

    // Helpers for Images & Permissions
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempUri by remember { mutableStateOf<android.net.Uri?>(null) }

    // Image Picker (Gallery)
    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            scope.launch(kotlinx.coroutines.Dispatchers.IO) {
                val internalPath = ImageUtils.copyImageToInternalStorage(context, it)
                if (internalPath != null) {
                    imagePath = internalPath
                }
            }
        }
    }

    // Camera Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            imagePath = tempUri!!.toString()
        }
    }

    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = java.io.File(context.filesDir, "wish_images/img_${System.currentTimeMillis()}.jpg")
            file.parentFile?.mkdirs()
            val uri = androidx.core.content.FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                file
            )
            tempUri = uri
            cameraLauncher.launch(uri)
        } else {
            android.widget.Toast.makeText(context, "Permission Required", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // Audio Recorder
    val audioRecorder = remember { AudioRecorder(context) }
    val isRecording = remember { mutableStateOf(false) }

    // Dialog for Image Source
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Choose Image Source") },
            text = { Text("Select where to capture the image from.") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                         val file = java.io.File(context.filesDir, "wish_images/img_${System.currentTimeMillis()}.jpg")
                         file.parentFile?.mkdirs()
                         val uri = androidx.core.content.FileProvider.getUriForFile(
                            context,
                            context.packageName + ".fileprovider",
                            file
                        )
                        tempUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    imagePicker.launch("image/*")
                }) {
                    Text("Gallery")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background))
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Edit Wish",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )
            Spacer(modifier = Modifier.height(20.dp))
            
            // Show Error if any
            if (homeState.error != null) {
                Text(
                    text = homeState.error ?: "Unknown Error",
                    color = colorResource(R.color.serene_error),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // IMAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showImageSourceDialog = true }
            ) {
                if (!imagePath.isNullOrBlank()) {
                    AsyncImage(
                        model = imagePath,
                        contentDescription = "Wish Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                     Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.serene_primary).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                         Image(
                             painter = painterResource(id = R.mipmap.image_placeholder),
                             contentDescription = null,
                             modifier = Modifier.fillMaxSize(),
                             contentScale = ContentScale.Crop
                         )
                         Text(
                             text = "Tap to add image",
                             color = colorResource(R.color.serene_text_primary),
                             fontWeight = FontWeight.Bold,
                             modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
                         )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // TITLE
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // NOTE
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically, 
                modifier = Modifier.fillMaxWidth()
            ) {
                 MediaButton(
                    text = if (isRecording.value) "Stop Recording" else {
                         if (audioPath != null) "Record New Audio" else "Record Voice"
                    },
                    isRecording = isRecording.value
                ) {
                    if (isRecording.value) {
                        audioRecorder.stop()
                        isRecording.value = false
                        android.widget.Toast.makeText(context, "Voice note recorded", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        val audioFile = java.io.File(context.filesDir, "audio_${System.currentTimeMillis()}.mp3")
                        audioRecorder.start(audioFile)
                        audioPath = audioFile.absolutePath
                        isRecording.value = true
                        android.widget.Toast.makeText(context, "Recording...", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            if (!audioPath.isNullOrBlank() && !isRecording.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { playAudio(audioPath!!) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.serene_primary)
                    )
                ) {
                    Text(
                        text = "Play Voice Note ▷",
                        modifier = Modifier.padding(16.dp),
                        color = colorResource(R.color.white),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.weight(1f))
            
            // SAVE BUTTON
            Button(
                onClick = {
                    val updatedWish = wish!!.copy(
                        title = title,
                        note = note,
                        imagePath = imagePath,
                        audioPath = audioPath,
                        timestamp = System.currentTimeMillis()
                    )
                    wishViewModel.editWish(updatedWish)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.serene_primary)
                ),
                enabled = !homeState.isLoading
            ) {
                if (homeState.isLoading) {
                    CircularProgressIndicator(color = colorResource(R.color.white), modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", color = colorResource(R.color.white))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    wishViewModel.deleteWish(wish!!.id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.serene_error)
                ),
                enabled = !homeState.isLoading
            ) {
                 if (homeState.isLoading) {
                    CircularProgressIndicator(color = colorResource(R.color.white), modifier = Modifier.size(24.dp))
                } else {
                    Text("Delete Wish", color = colorResource(R.color.white))
                }
            }
        }
    }
}
