package com.codeturtle.routes

import com.codeturtle.data.model.auth.SimpleResponse
import com.codeturtle.data.model.note.Note
import com.codeturtle.repository.NoteRepo
import com.codeturtle.routes.utils.RouteConstants.CREATE_NOTE
import com.codeturtle.routes.utils.RouteConstants.DELETE_NOTE
import com.codeturtle.routes.utils.RouteConstants.NOTES
import com.codeturtle.routes.utils.RouteConstants.UPDATE_NOTE
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes(
    db: NoteRepo
) {
    authenticate {
        post(CREATE_NOTE) {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = SimpleResponse(
                        success = false,
                        message = "missing fields"
                    )
                )
                return@post
            }

            try {
                val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
                if (!email.isNullOrEmpty()) {
                    db.addNote(note, email)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = SimpleResponse(
                            success = true,
                            message = "Note Added Successfully!!"
                        )
                    )
                }else{
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = SimpleResponse(
                            success = false,
                            message = "email authentication failed"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = SimpleResponse(
                        success = false,
                        message = e.message ?: "Some problem occurred"
                    )
                )
            }
        }

        get(NOTES){
            try {
                val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
                if (!email.isNullOrEmpty()) {
                    val notes = db.getAllNotes(email)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = notes
                    )
                }
            } catch (e:Exception){
                call.respond(
                    status = HttpStatusCode.NoContent,
                    message = emptyList<Note>()
                )
            }
        }

        post(UPDATE_NOTE) {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = SimpleResponse(
                        success = false,
                        message = "missing fields"
                    )
                )
                return@post
            }

            try {
                val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
                if (!email.isNullOrEmpty()) {
                    db.updateNote(note, email)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = SimpleResponse(
                            success = true,
                            message = "Note Updated Successfully!!"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = SimpleResponse(
                        success = false,
                        message = e.message ?: "Some problem occurred"
                    )
                )
            }
        }

        delete(DELETE_NOTE) {
            val noteId = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = SimpleResponse(
                        success = false,
                        message = "QueryParameters: id is not present"
                    )
                )
                return@delete
            }

            try {
                val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
                if (!email.isNullOrEmpty()) {
                    db.deleteNote(noteId,email)
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = SimpleResponse(
                            success = true,
                            message = "Note Deleted Successfully"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = SimpleResponse(
                        success = false,
                        message = e.message ?: "Some problem occurred"
                    )
                )
            }
        }
    }
}