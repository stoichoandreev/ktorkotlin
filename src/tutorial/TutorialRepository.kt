package com.sniper.ktor.tutorial

import com.sniper.ktor.EntityNotFoundError
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

object TutorialRepository {
    private val counter = AtomicInteger()
    private val tutorialList = CopyOnWriteArraySet<Tutorial>()

    fun add(tutorial: Tutorial): Tutorial {
        return when {
            tutorialList.contains(tutorial) -> tutorialList.find { it == tutorial }!!
            else -> with(tutorial) {
                id = counter.incrementAndGet()
                tutorialList.add(this)
                this
            }
        }
    }

    fun get(id: String): Tutorial {
        return tutorialList.find { id == it.id.toString() }
            ?: throw EntityNotFoundError(id)
    }

    fun get(id: Int): Tutorial = get(id.toString())

    fun getAll(): List<Tutorial> = tutorialList.toList()

    fun remove(tutorial: Tutorial): Boolean {
        return when {
            !tutorialList.contains(tutorial) -> throw EntityNotFoundError(tutorial.id.toString())
            else ->
            tutorialList.remove(tutorial)
        }
    }

    fun remove(id: String): Boolean = remove(get(id))

    fun remove(id: Int): Boolean = remove(get(id))

    fun clearAll() = tutorialList.clear()

}
