package com.example.calmlist.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.calmlist.R
import com.example.calmlist.model.UserData
import com.example.calmlist.navigation.Routes
import com.example.calmlist.presentation.ViewModel.AppViewModel

@Composable
fun SignUpScreen(viewModel: AppViewModel, navController: NavController){
    val state = viewModel.signupScreenstate.value

    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }
    var showText by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.serene_primary),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Signing you up...",
                        color = colorResource(R.color.serene_text_secondary),
                        fontSize = 13.sp
                    )
                }
            }

            !state.error.isNullOrEmpty() -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Something went wrong",
                        color = colorResource(R.color.serene_text_primary),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        state.error ?: "",
                        color = colorResource(R.color.serene_error),
                        fontSize = 13.sp
                    )
                }
            }

            else -> {
                if (state.success == true) {
                    LaunchedEffect(Unit) {
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(Routes.LogINScreen) { inclusive = true }
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Create Account",
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
                        )
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
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    if (showText) {
                        Text(
                            "Enter a valid email and password",
                            color = colorResource(R.color.serene_error),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    Button(
                        onClick = {
                            if (userEmail.isNotBlank() && userPassword.isNotBlank()) {
                                viewModel.createUser(
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
                        )
                    ) {
                        Text(
                            "Sign In",
                            color = colorResource(R.color.white),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Already have an account? Sign In",
                        fontSize = 13.sp,
                        color = colorResource(R.color.serene_text_secondary),
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.LogINScreen)
                        }
                    )
                }
            }
        }
    }

}