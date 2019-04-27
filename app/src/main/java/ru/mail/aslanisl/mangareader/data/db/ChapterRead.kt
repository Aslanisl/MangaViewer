package ru.mail.aslanisl.mangareader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter_read")
data class ChapterRead(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val mangaId: String,
    val chapterId: String
)