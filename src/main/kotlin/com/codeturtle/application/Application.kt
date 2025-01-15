package com.codeturtle.application

import com.codeturtle.repository.DatabaseFactory
import com.codeturtle.repository.UserRepo
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //Initializing database
    DatabaseFactory.init()

    val db = UserRepo()

    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureRouting()
}
