package com.codeturtle.data.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table("User") {
    val email = varchar(name = "email", length = 512)
    val name = varchar("name",512)
    val hashPassword = varchar("hashPassword",512)

    override val primaryKey = PrimaryKey(email)
}