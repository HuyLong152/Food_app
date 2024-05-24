package com.example.food_app.composegooglesignincleanarchitecture.presentation.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
