package com.sniper.ktor.comments

data class Comment (val text: String, val personId: Int) {
    var id: Int? = null
}
