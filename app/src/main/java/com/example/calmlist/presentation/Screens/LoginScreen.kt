package com.example.calmlist.presentation.Screens

import com.example.calmlist.presentation.ViewModel.AppViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calmlist.R
import androidx.navigation.NavController
import com.example.calmlist.model.UserData
import com.example.calmlist.navigation.Routes

@Composable
fun LogInScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val state = viewModel.logINScreenstate.value

    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }
    var showText by rememberSaveable { mutableStateOf(false) }

    // Handle Success Navigation
    LaunchedEffect(state.success) {
        if (state.success == true) {
            navController.navigate(Routes.HomeScreen) {
                popUpTo(Routes.LogINScreen) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                "Welcome back",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                placeholder = {
                    Text("Email", color = colorResource(R.color.serene_hint))
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.serene_primary),
                    unfocusedBorderColor = colorResource(R.color.serene_divider),
                    cursorColor = colorResource(R.color.serene_primary),
                    focusedTextColor = colorResource(R.color.serene_text_primary),
                    unfocusedTextColor = colorResource(R.color.serene_text_primary)
                ),
                enabled = !state.isLoading
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = userPassword,
                onValueChange = { userPassword = it },
                placeholder = {
                    Text("Password", color = colorResource(R.color.serene_hint))
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.serene_primary),
                    unfocusedBorderColor = colorResource(R.color.serene_divider),
                    cursorColor = colorResource(R.color.serene_primary),
                    focusedTextColor = colorResource(R.color.serene_text_primary),
                    unfocusedTextColor = colorResource(R.color.serene_text_primary)
                ),
                enabled = !state.isLoading
            )

            Spacer(Modifier.height(10.dp))

            if (showText) {
                Text(
                    "Enter a valid email and password",
                    color = colorResource(R.color.serene_error),
                    fontSize = 12.sp
                )
            }
            
            // Show Validation/Network Error
            if (!state.error.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.error,
                    color = colorResource(R.color.serene_error),
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    if (userEmail.isNotBlank() && userPassword.isNotBlank()) {
                         // Calls ViewModel, which sets Loading state, clearing error
                        viewModel.LoginUser(
                            UserData(
                                email = userEmail,
                                password = userPassword
                            )
                        )
                    } else {
                        showText = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.serene_primary)
                ),
                enabled = !state.isLoading
            ) {
                 if (state.isLoading) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.white),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "Sign In",
                        color = colorResource(R.color.white),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Don’t have an account? Sign Up",
                fontSize = 13.sp,
                color = colorResource(R.color.serene_text_secondary),
                modifier = Modifier.clickable(enabled = !state.isLoading) {
                    navController.navigate(Routes.SignUPScreen)
                }
            )
        }
    }
}
