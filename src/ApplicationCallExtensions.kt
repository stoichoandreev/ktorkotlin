package com.sniper.ktor

import io.ktor.application.*

fun <T> ApplicationCall.getParamValueOrNull(paramName: String): T? {
    return parameters[paramName] as T
}

fun <T> ApplicationCall.getParamValueOrException(paramName: String): T {
    return parameters[paramName] as T ?: throw ParameterNotFoundError()
}