package com.codeturtle.repository

import com.codeturtle.data.model.auth.User

interface UserDao {
    suspend fun addUser(user: User): User?
    suspend fun findUserByEmail(email:String): User?
}