package com.sniper.ktor

class ParameterNotFoundError(
    errorMessage: String = "Required request parameter not found"
): IllegalArgumentException(errorMessage)

class EntityNotFoundError(
    private val id: String,
    errorMessage: String = "No entity found for $id"
): IllegalArgumentException(errorMessage)