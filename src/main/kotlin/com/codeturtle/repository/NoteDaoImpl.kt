package com.codeturtle.repository

import com.codeturtle.data.model.note.Note
import com.codeturtle.data.table.NoteTable
import com.codeturtle.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteDaoImpl : NoteDao {
    override suspend fun addNote(note: Note, email: String): Note? = dbQuery {
        val insertStatement = NoteTable.insert { nt ->
            nt[userEmail] = email
            nt[noteTitle] = note.noteTitle
            nt[description] = note.description
            nt[date] = note.date
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::rowToNote)
    }

    override suspend fun getAllNotes(email: String): List<Note> = dbQuery {
        NoteTable.selectAll().where { NoteTable.userEmail.eq(email) }
            .mapNotNull { rowToNote(it) }
    }

    override suspend fun updateNote(note: Note, email: String): Boolean = dbQuery {
        NoteTable.update(
            where = {
                NoteTable.userEmail.eq(email) and NoteTable.id.eq(note.id)
            }
        ) { nt ->
            nt[noteTitle] = note.noteTitle
            nt[description] = note.description
            nt[date] = note.date
        } > 0
    }

    override suspend fun deleteNote(id: Int, email: String): Boolean = dbQuery {
        NoteTable.deleteWhere {
            userEmail.eq(email) and NoteTable.id.eq(id)
        } > 0
    }

    private fun rowToNote(row: ResultRow) = Note(
        id = row[NoteTable.id],
        noteTitle = row[NoteTable.noteTitle],
        description = row[NoteTable.description],
        date = row[NoteTable.date]
    )
}