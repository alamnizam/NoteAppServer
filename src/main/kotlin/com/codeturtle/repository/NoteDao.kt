package com.codeturtle.repository

import com.codeturtle.data.model.note.Note

interface NoteDao {
    suspend fun addNote(note: Note, email: String) : Note?
    suspend fun getAllNotes(email: String) : List<Note>
    suspend fun updateNote(note: Note, email: String) : Boolean
    suspend fun deleteNote(id: Int, email: String) : Boolean
}