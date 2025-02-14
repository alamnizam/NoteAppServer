package com.codeturtle.routes

import com.codeturtle.authentication.JWTService
import com.codeturtle.data.model.auth.LoginRequest
import com.codeturtle.data.model.auth.RegisterRequest
import com.codeturtle.data.model.SimpleResponse
import com.codeturtle.data.model.auth.User
import com.codeturtle.repository.UserDao
import com.codeturtle.routes.utils.RouteConstants.LOGIN_REQUEST
import com.codeturtle.routes.utils.RouteConstants.REGISTER_REQUEST
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.userRoutes(
    db: UserDao,
    mJWTService: JWTService,
    hashFunction: (String) -> String
) {
    post(REGISTER_REQUEST) {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest, SimpleResponse(
                    success = false,
                    message = "missing fields"
                )
            )
            return@post
        }

        try {
            val user = User(registerRequest.email, hashFunction(registerRequest.password), registerRequest.name)
            db.addUser(user)
            call.respond(
                HttpStatusCode.OK, SimpleResponse(
                    success = true,
                    message = mJWTService.generateToken(user)
                )
            )
        } catch (ex: ExposedSQLException) {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = SimpleResponse(
                    success = false,
                    message = "(${registerRequest.email})-email already exists"
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict, SimpleResponse(
                    success = false,
                    message = e.message ?: "Some problem occurred"
                )
            )
        }
    }

    post(LOGIN_REQUEST) {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest, SimpleResponse(
                    success = false,
                    message = "missing fields"
                )
            )
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)

            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest, SimpleResponse(
                        success = false,
                        message = "Wrong email/password"
                    )
                )
            } else {
                if (user.hashPassword == hashFunction(loginRequest.password)) {
                    call.respond(
                        HttpStatusCode.OK, SimpleResponse(
                            success = true,
                            message = mJWTService.generateToken(user)
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, SimpleResponse(
                            success = false,
                            message = "Wrong email/password"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict, SimpleResponse(
                    success = false,
                    message = e.message ?: "Some problem occurred"
                )
            )
        }
    }
}