package com.codeturtle.repository

import com.codeturtle.data.table.NoteTable
import com.codeturtle.data.table.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(){
       val database =  Database.connect(
           url = "jdbc:postgresql:note_db",
           driver = "org.postgresql.Driver",
           user = "postgres",
           password = "Nizam@123"
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