package ru.mail.aslanisl.mangareader.data.model

data class Chapter(
    val id: String,
    val title: String,
    var readed: Boolean = false
)