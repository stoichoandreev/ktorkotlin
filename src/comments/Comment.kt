package com.sniper.ktor.comments

import com.sniper.ktor.common.Entity


data class Comment(val text: String, val personId: Int): Entity()
