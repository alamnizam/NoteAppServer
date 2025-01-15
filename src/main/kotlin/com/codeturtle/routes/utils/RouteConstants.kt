package com.codeturtle.routes.utils

object RouteConstants {
    private const val API_VERSION = "/v1"
    private const val USERS = "$API_VERSION/users"
    const val REGISTER_REQUEST = "$USERS/register"
    const val LOGIN_REQUEST = "$USERS/login"
}