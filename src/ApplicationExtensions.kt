package com.sniper.ktor

import com.sniper.ktor.comments.CommentRepository
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
                val responseData = PersonRepository.get(id)
                call.respond(responseData)
            }
        }

        //Get All People
        get(PERSON_ALL_ENDPOINT) {
            errorAware {
                val responseData = PersonRepository.getAll()
                call.respond(responseData)
            }
        }

        //Delete Person by id
        delete(PERSON_ENDPOINT) {
            errorAware {
                val id: String = call.getParamValueOrException(PARAM_ID)
                val responseData = PersonRepository.remove(id)
                call.respond(responseData)
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
                val responseData = TutorialRepository.get(id)
                call.respond(responseData)
            }
        }

        //Get All Tutorials
        get(TUTORIAL_ALL_ENDPOINT) {
            errorAware {
                val responseData = TutorialRepository.getAll()
                call.respond(responseData)
            }
        }

        //Delete Tutorial by id
        delete(TUTORIAL_ENDPOINT) {
            errorAware {
                val id: String = call.getParamValueOrException(PARAM_ID)
                val responseData = TutorialRepository.remove(id)
                call.respond(responseData)
            }
        }

        //Delete all Tutorials
        delete(TUTORIAL_ALL_ENDPOINT) {
            errorAware {
                TutorialRepository.clearAll()
                call.respondSuccessfulJson()
            }
        }

        //Get Comment by id
        get(COMMENT_ENDPOINT) {
            errorAware {
                val id: String = call.getParamValueOrException(PARAM_ID)
                val responseData = CommentRepository.get(id)
                call.respond(responseData)
            }
        }

        //Get All Comments
        get(COMMENT_ALL_ENDPOINT) {
            errorAware {
                val responseData = CommentRepository.getAll()
                call.respond(responseData)
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