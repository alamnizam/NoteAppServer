package com.codeturtle.application

import com.codeturtle.repository.DatabaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //Initializing database
    val url = this.environment.config.property("db.url").getString()
    val user = this.environment.config.property("db.user").getString()
    val password = this.environment.config.property("db.password").getString()
    val driver = this.environment.config.property("db.driver").getString()
    DatabaseFactory.init(url,user,password,driver)

    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureRouting()
}
