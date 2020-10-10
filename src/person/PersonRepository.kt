package com.sniper.ktor.person

import com.sniper.ktor.EntityNotFoundError
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

object PersonRepository {
    private val counter = AtomicInteger()
    private val personList = CopyOnWriteArraySet<Person>()

    fun add(person: Person): Person {
        return when {
            personList.contains(person) -> personList.find { it == person }!!
            else -> with(person) {
                id = counter.incrementAndGet()
                personList.add(this)
                this
            }
        }
    }

    fun get(id: String): Person {
        return personList.find { id == it.id.toString() }
            ?: throw EntityNotFoundError(id)
    }

    fun get(id: Int): Person = get(id.toString())

    fun getAll(): List<Person> = personList.toList()

    fun remove(person: Person): Boolean {
        return when {
            !personList.contains(person) -> throw EntityNotFoundError(person.id.toString())
            else ->
            personList.remove(person)
        }
    }

    fun remove(id: String): Boolean = remove(get(id))

    fun remove(id: Int): Boolean = remove(get(id))

}
