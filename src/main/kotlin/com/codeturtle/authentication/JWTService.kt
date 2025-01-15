package com.codeturtle.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.codeturtle.data.model.User

class JWTService {
    private val issuer = "noteServer"
    private val mJWTSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(mJWTSecret)

    val verifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String {
        return JWT.create()
            .withSubject("NoteAuthentication")
            .withIssuer(issuer)
            .withClaim("email",user.email)
            .sign(algorithm)
    }
}