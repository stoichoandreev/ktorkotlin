package com.sniper.ktor

import com.sniper.ktor.person.PersonRepository
import com.sniper.ktor.tutorial.TutorialRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import java.text.DateFormat
import java.time.Duration

fun Application.main() {
    install(DefaultHeaders)
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    routing {
        //Get Person by id
        get(PERSON_ENDPOINT) {
            errorAware {
                val id: String = call.getParamValueOrException(PARAM_ID)
                call.respond(PersonRepository.get(id))
            }
        }
        //Get All People
        get(PERSON_ALL_ENDPOINT) {
            errorAware {
                call.respond(PersonRepository.getAll())
            }
        }
        //Delete Person by id
        delete(PERSON_ENDPOINT) {
            errorAware {
                val id: String = call.getParamValueOrException(PARAM_ID)
                call.respond(PersonRepository.remove(id))
            }
        }
        //Delete all People
        delete(PERSON_ALL_ENDPOINT) {
            errorAware {
                PersonRepository.clearAll()
                call.respondSuccessfulJson()
            }
        }

        //Get Tutorial by id
        get(TUTORIAL_ENDPOINT) {
            errorAware {
                val id: String = call.getParamValueOrException(PARAM_ID)
                call.respond(TutorialRepository.get(id))
            }
        }
        //Get All Tutorials
        get(TUTORIAL_ALL_ENDPOINT) {
            errorAware {
                call.respond(TutorialRepository.getAll())
            }
        }
    }
}

suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend () -> R): R? {
    return try {
        block()
    } catch (exception: Exception) {
        call.respondText(
            """{"error":"$exception"}""",
            ContentType.parse("application/json"),
            HttpStatusCode.InternalServerError
        )
        null
    }
}

suspend fun ApplicationCall.respondSuccessfulJson(value: Boolean = true) {
    return respond("""{"success":"$value"}""")
}