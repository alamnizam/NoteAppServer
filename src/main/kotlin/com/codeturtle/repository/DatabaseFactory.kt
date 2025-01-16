package com.codeturtle.repository

import com.codeturtle.data.table.NoteTable
import com.codeturtle.data.table.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(url: String, user: String, password: String, driver: String) {
       val database =  Database.connect(
           url = url,
           user = user,
           password = password,
           driver = driver
       )
        transaction(database) {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
        }
    }


    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}