package com.sniper.ktor.comments

import com.sniper.ktor.EntityNotFoundError
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

object CommentRepository {
    private val counter = AtomicInteger()
    private val commentsList = CopyOnWriteArraySet<Comment>()

    fun add(comment: Comment): Comment {
        return when {
            commentsList.contains(comment) -> commentsList.find { it == comment }!!
            else -> with(comment) {
                id = counter.incrementAndGet()
                commentsList.add(this)
                this
            }
        }
    }

    fun get(id: String): Comment {
        return commentsList.find { id == it.id.toString() }
            ?: throw EntityNotFoundError(id)
    }

    fun get(id: Int): Comment = get(id.toString())

    fun getAll(): List<Comment> = commentsList.toList()

    fun remove(comment: Comment): Boolean {
        return when {
            !commentsList.contains(comment) -> throw EntityNotFoundError(comment.id.toString())
            else ->
            commentsList.remove(comment)
        }
    }

    fun remove(id: String): Boolean = remove(get(id))

    fun remove(id: Int): Boolean = remove(get(id))

    fun clearAll() = commentsList.clear()

}
