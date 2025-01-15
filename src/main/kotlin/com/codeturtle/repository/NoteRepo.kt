package com.codeturtle.repository

import com.codeturtle.data.model.note.Note
import com.codeturtle.data.table.NoteTable
import com.codeturtle.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteRepo {
    suspend fun addNote(note: Note, email: String){
        dbQuery {
            NoteTable.insert { nt->
                nt[id] = note.id
                nt[userEmail] = email
                nt[noteTitle] = note.noteTitle
                nt[description] = note.description
                nt[date] = note.date
            }
        }
    }

    suspend fun getAllNotes(email:String):List<Note> = dbQuery {
        NoteTable.selectAll().where { NoteTable.userEmail.eq(email) }
            .mapNotNull { rowToNote(it) }

    }


    suspend fun updateNote(note:Note,email: String){

        dbQuery {

            NoteTable.update(
                where = {
                    NoteTable.userEmail.eq(email) and NoteTable.id.eq(note.id)
                }
            ){ nt->
                nt[noteTitle] = note.noteTitle
                nt[description] = note.description
                nt[date] = note.date
            }

        }

    }

    suspend fun deleteNote(id:String){
        dbQuery {
            NoteTable.deleteWhere { NoteTable.id.eq(id) }
        }
    }


    private fun rowToNote(row:ResultRow?): Note? {

        if(row == null){
            return null
        }

        return Note(
            id = row[NoteTable.id],
            noteTitle = row[NoteTable.noteTitle],
            description =  row[NoteTable.description],
            date = row[NoteTable.date]
        )

    }
}