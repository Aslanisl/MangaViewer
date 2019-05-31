package ru.mail.aslanisl.mangareader.source

enum class MangaFilter(var asc: Boolean = true) {
    NONE,
    DATE,
    FAVORITE,
    NAME,
    CHAPTER,
}