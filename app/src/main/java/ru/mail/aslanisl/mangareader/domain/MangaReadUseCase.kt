package ru.mail.aslanisl.mangareader.domain

import androidx.annotation.WorkerThread
import ru.mail.aslanisl.mangareader.data.db.MangaRead
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.db.dao.MangaReadDao

class MangaReadUseCase(private val mangaReadDao: MangaReadDao) {

    @WorkerThread
    fun setMangaRead(manga: Manga, sourceClazzName: String) {
        val mangaRead = MangaRead(
            manga.id,
            manga.photoUrl,
            manga.name,
            manga.description,
            sourceClazzName
        )
        mangaReadDao.setMangaRead(mangaRead)
    }

    @WorkerThread
    fun getReadManga(sourceClazzName: String) = mangaReadDao.getReadManga(sourceClazzName)
}