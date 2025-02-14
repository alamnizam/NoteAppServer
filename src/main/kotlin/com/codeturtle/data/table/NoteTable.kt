package com.codeturtle.data.table

import org.jetbrains.exposed.sql.Table

object NoteTable : Table() {
    val id = integer("id").autoIncrement()
    val userEmail = varchar("userEmail",512).references(UserTable.email)
    val noteTitle = text("noteTitle")
    val description = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}