package com.codeturtle.application

import com.codeturtle.repository.DatabaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //Initializing database
    DatabaseFactory.init()

    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureRouting()
}
