package com.codeturtle.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.codeturtle.data.model.auth.User
import io.ktor.server.auth.jwt.*

class JWTService(
    private val secret:String,
    private val issuer:String,
    private val audience:String
) {
    fun generateToken(user: User): String {
        return JWT.create()
            .withSubject("NoteAuthentication")
            .withIssuer(issuer)
            .withClaim("email",user.email)
            .sign(Algorithm.HMAC512(secret))
    }

    fun verify() : JWTVerifier = JWT
        .require(Algorithm.HMAC512(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun validate(jwtCredential: JWTCredential) : JWTPrincipal?{
        return jwtCredential.payload.getClaim("email").asString()
            .takeIf { !it.isNullOrEmpty() }
            ?.let { JWTPrincipal(jwtCredential.payload) }
    }
}