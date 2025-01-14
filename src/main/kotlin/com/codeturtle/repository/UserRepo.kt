package com.codeturtle.repository

import com.codeturtle.data.model.User
import com.codeturtle.data.table.UserTable
import com.codeturtle.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll


class UserRepo {
    suspend fun addUser(user: User){
        dbQuery{
            UserTable.insert { ut->
                ut[email] = user.email
                ut[hashPassword] = user.hashPassword
                ut[name] = user.name
            }
        }
    }

    suspend fun findUserByEmail(email:String) = dbQuery {
        UserTable.selectAll().where { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row:ResultRow?):User?{
        if(row == null){
            return null
        }

        return User(
            email =  row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            name = row[UserTable.name]
        )
    }
}