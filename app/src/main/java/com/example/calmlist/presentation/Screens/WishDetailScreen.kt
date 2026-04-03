package com.example.calmlist.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel
import com.example.calmlist.util.playAudio

@Composable
fun WishDetailScreen(navController: NavHostController, wishViewModel: WishDetailViewModel) {


    val wish by wishViewModel.selectedWish

    if (wish == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var title by remember { mutableStateOf(wish!!.title ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // IMAGE
        if (!wish!!.imagePath.isNullOrBlank()) {
            AsyncImage(
                model = wish!!.imagePath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            PlaceholderBox("No Image Added")
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (!wish!!.audioPath.isNullOrBlank()) {
            Button(onClick = {
                playAudio(wish!!.audioPath!!)
            }) {
                Text("Play Voice Note")
            }
        } else {
            PlaceholderBox("No Voice Note")
        }

        Spacer(modifier = Modifier.height(20.dp))


        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Edit Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val updatedWish = wish!!.copy(
                    title = title,
                    timestamp = System.currentTimeMillis()
                )
                wishViewModel.editWish(updatedWish)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                wishViewModel.deleteWish(wish!!.id)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete")
        }
    }
}
@Composable
fun PlaceholderBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}
