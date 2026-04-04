package com.example.calmlist.presentation.Screens

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
import androidx.compose.runtime.Composable
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

@Composable
fun WishDetailScreen(navController: NavHostController, wishViewModel: WishDetailViewModel) {
    val wish by wishViewModel.selectedWish
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
    var title by remember { mutableStateOf(wish!!.title ?: "") }
    var note by remember { mutableStateOf(wish!!.note ?: "") }
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
            // IMAGE
            if (!wish!!.imagePath.isNullOrBlank()) {
                AsyncImage(
                    model = wish!!.imagePath,
                    contentDescription = "Wish Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
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
            // AUDIO
            if (!wish!!.audioPath.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { playAudio(wish!!.audioPath!!) },
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
                        timestamp = System.currentTimeMillis()
                    )
                    wishViewModel.editWish(updatedWish)
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
                Text("Save Changes", color = colorResource(R.color.white))
            }
            Spacer(modifier = Modifier.height(12.dp))
            // DELETE BUTTON
            Button(
                onClick = {
                    wishViewModel.deleteWish(wish!!.id)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.serene_error)
                )
            ) {
                Text("Delete Wish", color = colorResource(R.color.white))
            }
        }
    }
}
