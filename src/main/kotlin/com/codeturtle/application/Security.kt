package com.codeturtle.application

import com.codeturtle.authentication.JWTService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    val mJWTService = JWTService(
        secret = System.getenv("JWT_SECRET"),
        issuer = this.environment.config.property("jwt.issuer").getString(),
        audience = this.environment.config.property("jwt.audience").getString()
    )
    authentication {
        jwt {
            verifier(mJWTService.verify())

            validate { jwtCredential ->
                mJWTService.validate(jwtCredential)
            }
            
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized,"Token is invalid")
            }
        }
    }
    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
@Serializable
data class MySession(val count: Int = 0)
