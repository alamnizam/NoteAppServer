package com.codeturtle.repository

import com.codeturtle.data.table.NoteTable
import com.codeturtle.data.table.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(url: String, user: String, password: String, driver: String) {
        val database = Database.connect(
            url = url,
            user = user,
            password = password,
            driver = driver
        )
        transaction(database) {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)

            // Fix auto-increment for note table if it exists
            try {
                println("Applying auto-increment migration for note table...")

                // Create sequence if it doesn't exist
                exec("CREATE SEQUENCE IF NOT EXISTS note_id_seq")

                // Set the sequence to start from the max id + 1
                // Cast id to integer to handle type mismatch
                exec("SELECT setval('note_id_seq', COALESCE((SELECT MAX(CAST(id AS INTEGER)) FROM note), 0) + 1, false)")

                // Set the default value for id column to use the sequence
                exec("ALTER TABLE note ALTER COLUMN id SET DEFAULT nextval('note_id_seq')")

                // Change the column type to integer if it's not already
                try {
                    exec("ALTER TABLE note ALTER COLUMN id TYPE INTEGER USING CAST(id AS INTEGER)")
                } catch (e: Exception) {
                    println("Note: Column type already correct or cannot be changed: ${e.message}")
                }

                // Associate the sequence with the column (so it gets dropped with the table)
                exec("ALTER SEQUENCE note_id_seq OWNED BY note.id")

                println("Auto-increment migration completed successfully!")
            } catch (e: Exception) {
                // If migration fails, log the full error
                println("ERROR: Could not apply auto-increment migration: ${e.message}")
                e.printStackTrace()
            }
        }
    }


    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}