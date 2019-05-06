package ru.mail.aslanisl.mangareader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mail.aslanisl.mangareader.data.db.ChapterRead
import ru.mail.aslanisl.mangareader.data.db.MangaRead
import ru.mail.aslanisl.mangareader.db.dao.ChapterReadDao
import ru.mail.aslanisl.mangareader.db.dao.MangaReadDao

private const val VERSION = 4

@Database(entities = [ChapterRead::class, MangaRead::class], version = VERSION, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun chapterReadDao(): ChapterReadDao
    abstract fun mangaReadDao(): MangaReadDao
}