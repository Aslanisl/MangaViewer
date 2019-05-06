package ru.mail.aslanisl.mangareader.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mail.aslanisl.mangareader.data.db.ChapterRead

@Dao
interface ChapterReadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setChapterRead(chapterRead: ChapterRead)

    @Query("SELECT * FROM chapter_read WHERE mangaId = :mangaId")
    fun getReadChapter(mangaId: String): List<ChapterRead>
}