package ru.mail.aslanisl.mangareader.features.details

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.db.ChapterRead
import ru.mail.aslanisl.mangareader.db.dao.ChapterReadedDao
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class DetailsViewModel constructor(
    private val source: IMangaSource,
    private val chapterReadedDao: ChapterReadedDao
): BaseViewModel() {

    fun loadChapters(idManga: String): LiveData<UIData<List<Chapter>>> {
        val liveData = getLoadingLiveData<List<Chapter>>()
        launch {
            val chapters = source.loadChapter(idManga)
            val readedChapter = chapterReadedDao.getReadedChapter(idManga)
            chapters.body?.forEach { chapter ->
                chapter.readed = readedChapter.firstOrNull { it.chapterId == chapter.id } != null
            }
            liveData.postValue(chapters)
        }
        return liveData
    }

    fun setChapterReaded(mangaId: String, chapterId: String) {
        launch {
            chapterReadedDao.setChapterReaded(ChapterRead(mangaId = mangaId, chapterId = chapterId))
        }
    }
}