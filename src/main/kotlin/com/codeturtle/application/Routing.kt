package com.codeturtle.application

import com.codeturtle.authentication.JWTService
import com.codeturtle.authentication.hash
import com.codeturtle.data.model.User
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val mJWTService = JWTService()
    val hashFunction = { secret: String -> hash(secret) }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/token"){
            val email = call.request.queryParameters["email"]!!
            val password = call.request.queryParameters["password"]!!
            val username = call.request.queryParameters["username"]!!

            val user = User(email,hashFunction(password),username)
            call.respond(mJWTService.generateToken(user))
        }
    }
}