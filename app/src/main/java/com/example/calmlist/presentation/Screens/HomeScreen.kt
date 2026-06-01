package com.example.calmlist.presentation.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calmlist.R
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.Wish
import com.example.calmlist.navigation.Routes
import com.example.calmlist.presentation.ViewModel.AppViewModel


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
        ) { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "Home",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )
            IconButton(onClick = {
                navController.navigate(Routes.SettingsScreen)
            }) {
                Icon(imageVector = Icons.Filled.Settings,contentDescription = null)
            }

        }

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
                    items(
                        homeState.wishes.sortedByDescending { it.timestamp }
                    ) { wish ->
                        WishCard(
                            wish = wish,
                            wishDetailViewModel = wishViewModel,

                            navController

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
    wishDetailViewModel: WishDetailViewModel,
    navController: NavController

) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.serene_primary)
        ),
        modifier = Modifier
            .fillMaxWidth().height(150.dp)
            .clickable {
                wishDetailViewModel.setSelectedWishId(wish.id)
                navController.navigate(Routes.WishDetailScreen)

                }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
//
//            if (!wish.imagePath.isNullOrBlank()) {
//                AsyncImage(
//                    model = wish.imagePath,
//                    contentDescription = "Wish Image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//            }

            Text(
                text = wish.title ?: "Untitled Wish",
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,

                color = colorResource(R.color.serene_text_primary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!wish.note.isNullOrBlank()) {
                Text(
                    text = wish.note,
                    fontSize = 23.sp,
                    color = colorResource(R.color.serene_text_secondary),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {



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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No wishes yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap + to capture something you want",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )
        }
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
    val time = java.text.SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        java.util.Locale.getDefault()
    )
    return time.format(java.util.Date(timestamp))
}
