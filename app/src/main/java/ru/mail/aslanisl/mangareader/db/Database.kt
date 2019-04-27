package ru.mail.aslanisl.mangareader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mail.aslanisl.mangareader.data.db.ChapterRead
import ru.mail.aslanisl.mangareader.db.dao.ChapterReadedDao

private const val VERSION = 1

@Database(entities = [ChapterRead::class], version = VERSION, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun chapterReadedDao(): ChapterReadedDao
}