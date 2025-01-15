package com.codeturtle.data.model.auth

data class RegisterRequest(
    val email:String,
    val name:String,
    val password:String
)
