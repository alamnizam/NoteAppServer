package com.codeturtle.application

import com.codeturtle.authentication.JWTService
import com.codeturtle.authentication.hash
import com.codeturtle.data.model.auth.User
import com.codeturtle.repository.NoteRepo
import com.codeturtle.repository.UserRepo
import com.codeturtle.routes.noteRoutes
import com.codeturtle.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    val userDB = UserRepo()
    val noteDB = NoteRepo()
    val mJWTService = JWTService(
        secret = System.getenv("JWT_SECRET"),
        issuer = this.environment.config.property("jwt.issuer").getString(),
        audience = this.environment.config.property("jwt.audience").getString()
    )
    val hashFunction = { secret: String -> hash(secret) }
    routing {
        userRoutes(userDB, mJWTService, hashFunction)
        noteRoutes(noteDB)

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