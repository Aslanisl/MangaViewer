package ru.mail.aslanisl.mangareader.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mail.aslanisl.mangareader.data.db.MangaRead

@Dao
interface MangaReadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMangaRead(mangaRead: MangaRead)

    @Query("SELECT * FROM manga_read WHERE sourceClazzName = :sourceClazzName ORDER BY timeStamp DESC")
    fun getReadManga(sourceClazzName: String): List<MangaRead>
}