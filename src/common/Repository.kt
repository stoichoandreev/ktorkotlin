package com.sniper.ktor.common

import com.sniper.ktor.EntityNotFoundError
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

open class Repository<T: Entity> {
    private val counter = AtomicInteger()
    private val entityList = CopyOnWriteArraySet<T>()

    fun add(entity: T): T {
        return when {
            entityList.contains(entity) -> entityList.find { it == entity }!!
            else -> with(entity) {
                id = counter.incrementAndGet()
                entityList.add(this)
                this
            }
        }
    }

    fun get(id: String): T {
        return entityList.find { id == it.id.toString() }
            ?: throw EntityNotFoundError(id)
    }

    fun get(id: Int): T = get(id.toString())

    fun getAll(): List<T> = entityList.toList()

    fun remove(entity: T): Boolean {
        return when {
            !entityList.contains(entity) -> throw EntityNotFoundError(entity.id.toString())
            else ->
            entityList.remove(entity)
        }
    }

    fun remove(id: String): Boolean = remove(get(id))

    fun remove(id: Int): Boolean = remove(get(id))

    fun clearAll() = entityList.clear()

}
