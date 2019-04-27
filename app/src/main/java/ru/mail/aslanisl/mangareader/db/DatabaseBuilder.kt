package ru.mail.aslanisl.mangareader.db

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {

    fun build(context: Context) = Room
        .databaseBuilder(context.applicationContext, Database::class.java, "manga_bd")
        .fallbackToDestructiveMigration()
        .build()
}