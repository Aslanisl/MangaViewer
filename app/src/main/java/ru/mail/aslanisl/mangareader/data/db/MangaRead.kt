package ru.mail.aslanisl.mangareader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "manga_read")
data class MangaRead(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val photoUrl: String?,
    val name: String,
    val description: String,
    val sourceClazzName: String,
    val timeStamp: Long = Date().time
)