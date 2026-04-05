package com.example.calmlist.presentation.Screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calmlist.R
import com.example.calmlist.presentation.ViewModel.AppViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.calmlist.util.AudioRecorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.size
import java.io.File
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel

@Composable
fun AddWishScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    wishViewModel: WishDetailViewModel
) {
    val state = wishViewModel.addWishState.value


    LaunchedEffect(state.success) {
        if (state.success) {
            wishViewModel.resetAddWishState()
            navController.popBackStack()
        }
    }

    val userId = appViewModel.userId.value ?: ""

    val context = LocalContext.current


    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            wishViewModel.updateImage(it.toString())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background))
            .padding(24.dp)
    ) {
        Column {

            Text(
                text = "Add Wish",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = state.title,
                onValueChange = { wishViewModel.updateTitle(it) },
                placeholder = { Text("Wish title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.note,
                onValueChange = { wishViewModel.updateNote(it) },
                placeholder = { Text("Why do you want this? (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                maxLines = 4
            )

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                MediaButton(text = "Add Photo") {
                    imagePicker.launch("image/*")
                }

                val recorder = remember { AudioRecorder(context) }
                val isRecording = remember { mutableStateOf(false) }

                MediaButton(
                    text = if (isRecording.value) "Stop Recording" else "Record Voice",
                    isRecording = isRecording.value
                ) {
                    if (isRecording.value) {
                        recorder.stop()
                        isRecording.value = false
                        Toast.makeText(context, "Voice note recorded", Toast.LENGTH_SHORT).show()
                    } else {
                        val audioFile = File(context.filesDir, "audio_${System.currentTimeMillis()}.mp3")
                        recorder.start(audioFile)
                        wishViewModel.updateAudio(audioFile.absolutePath)
                        isRecording.value = true
                        Toast.makeText(context, "Recording...", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            if (state.imagePath != null) {
                Text("📷 Image attached", fontSize = 12.sp)
            }

            if (state.audioPath != null) {
                Text("🎤 Voice note attached", fontSize = 12.sp)
            }

            if (!state.error.isNullOrEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(
                    state.error!!,
                    color = colorResource(R.color.serene_error),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    wishViewModel.submitWish(appViewModel.userId.value ?: "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.serene_primary)
                ),
                enabled = !state.isSaving
            ) {
                 if (state.isSaving) {

                    CircularProgressIndicator(color = colorResource(R.color.white), modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Save Hope", color = colorResource(R.color.white), fontSize = 18.sp)
                }
            }
        }
    }
}
@Composable

fun MediaButton(
    text: String,
    isRecording: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRecording) colorResource(R.color.serene_error) else colorResource(R.color.serene_primary)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(14.dp),
            color = colorResource(R.color.white),
            fontSize = 13.sp
        )
    }
}
