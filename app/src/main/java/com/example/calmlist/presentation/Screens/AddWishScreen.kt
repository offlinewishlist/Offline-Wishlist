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
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel

@Composable
fun AddWishScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    wishViewModel: WishDetailViewModel
) {
    val state = wishViewModel.addWishState.value
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
                MediaButton("Add Photo") {
                    imagePicker.launch("image/*")
                }

                MediaButton("Record Voice") {
                    Toast
                        .makeText(context, "Audio recording feature connected", Toast.LENGTH_SHORT)
                        .show()
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

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    wishViewModel.submitWish(userId)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.serene_primary)
                )
            ) {
                Text("Save Wish", color = colorResource(R.color.white))
            }
        }
    }
}
@Composable
fun MediaButton(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.serene_primary)
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
