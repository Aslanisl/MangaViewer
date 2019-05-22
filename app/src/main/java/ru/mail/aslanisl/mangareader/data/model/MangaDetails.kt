package ru.mail.aslanisl.mangareader.data.model

data class MangaDetails(
    val id: String,
    val photoUrl: String?,
    val name: String,
    val description: String,
    val genre: List<String>,
    val chapters: List<Chapter>
)