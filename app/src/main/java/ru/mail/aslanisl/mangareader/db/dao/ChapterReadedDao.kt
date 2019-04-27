package ru.mail.aslanisl.mangareader.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mail.aslanisl.mangareader.data.db.ChapterRead

@Dao
interface ChapterReadedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setChapterReaded(chapterRead: ChapterRead)

    @Query("SELECT * FROM chapter_read WHERE mangaId = :mangaId")
    fun getReadedChapter(mangaId: String): List<ChapterRead>
}