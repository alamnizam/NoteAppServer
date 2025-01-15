package com.codeturtle.data.model.auth

data class User(
    val email:String,
    val hashPassword:String,
    val name:String
)
