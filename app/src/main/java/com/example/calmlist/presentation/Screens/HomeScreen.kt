package com.example.calmlist.presentation.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calmlist.R
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.Wish
import com.example.calmlist.navigation.Routes
import com.example.calmlist.presentation.ViewModel.AppViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.calmlist.presentation.ViewModel.SyncViewModel
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    syncViewModel: SyncViewModel,
    wishViewModel: WishDetailViewModel,
    viewModel: AppViewModel
) {
    val homeState = wishViewModel.HomeScreensate.value
    val syncState by syncViewModel.syncState.collectAsState()
    val userId = viewModel.userId.value ?: ""




    LaunchedEffect(Unit) {

        syncViewModel.sync(userId)
        wishViewModel.getAllWishes(userId)

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Home",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )

            Spacer(modifier = Modifier.height(20.dp))


            if (homeState.wishes.isEmpty() &&
                syncState !is ResultState.Loading &&
                !homeState.isLoading
            ) {
                Log.d("wishes", "HomeScreen: ${wishViewModel.HomeScreensate.value}")
                EmptyState()
            } else {
                Log.d("wishes", "HomeScreen: ${wishViewModel.HomeScreensate.value}")

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(homeState.wishes) { wish ->
                        WishCard(
                            wish = wish,
                            onClick = {
                                navController.navigate(
                                    Routes.WishListScreen
                                )
                            }
                        )
                    }
                }
            }
        }


        FloatingActionButton(
            onClick = {
                navController.navigate(Routes.AddWishScreen)
            },
            containerColor = colorResource(R.color.serene_primary),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Wish",
                tint = colorResource(R.color.white)
            )
        }


        if (syncState is ResultState.Loading) {
            LoadingOverlay("Syncing your wishes...")
        }

        if (homeState.isLoading) {
            LoadingOverlay("Loading wishes...")
        }


        if (!homeState.error.isNullOrEmpty()) {
            ErrorBanner(homeState.error!!)
        }
    }
}
@Composable
fun WishCard(
    wish: Wish,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.serene_primary)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            
            if (!wish.imagePath.isNullOrBlank()) {
                AsyncImage(
                    model = wish.imagePath,
                    contentDescription = "Wish Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = wish.title ?: "Untitled Wish",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (!wish.note.isNullOrBlank()) {
                Text(
                    text = wish.note,
                    fontSize = 13.sp,
                    color = colorResource(R.color.serene_text_secondary),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!wish.imagePath.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Has Photo",
                        tint = colorResource(R.color.serene_text_secondary),
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (!wish.audioPath.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = "Has Audio",
                        tint = colorResource(R.color.serene_text_secondary),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = formatTime(wish.timestamp),
                    fontSize = 11.sp,
                    color = colorResource(R.color.serene_hint)
                )
            }
        }
    }
}
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No wishes yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(R.color.serene_text_primary)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap + to add your first wish",
            fontSize = 13.sp,
            color = colorResource(R.color.serene_text_secondary)
        )
    }
}
@Composable
fun LoadingOverlay(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background).copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = colorResource(R.color.serene_primary),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                fontSize = 13.sp,
                color = colorResource(R.color.serene_text_secondary)
            )
        }
    }
}
@Composable
fun ErrorBanner(message: String) {
    Box(
        modifier = Modifier

            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.serene_error)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(12.dp),
                fontSize = 13.sp,
                color = colorResource(R.color.white)
            )
        }
    }
}
fun formatTime(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
