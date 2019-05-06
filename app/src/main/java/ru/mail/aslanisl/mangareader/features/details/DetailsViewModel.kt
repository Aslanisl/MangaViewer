package ru.mail.aslanisl.mangareader.features.details

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.db.ChapterRead
import ru.mail.aslanisl.mangareader.db.dao.ChapterReadDao
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class DetailsViewModel constructor(
    private val source: IMangaSource,
    private val chapterReadDao: ChapterReadDao
): BaseViewModel() {

    fun loadChapters(idManga: String): LiveData<UIData<List<Chapter>>> {
        val liveData = getLoadingLiveData<List<Chapter>>()
        launch {
            val chapters = source.loadChapter(idManga)
            val readChapter = chapterReadDao.getReadChapter(idManga)
            chapters.body?.forEach { chapter ->
                chapter.readed = readChapter.firstOrNull { it.chapterId == chapter.id } != null
            }
            liveData.postValue(chapters)
        }
        return liveData
    }

    fun setChapterRead(mangaId: String, chapterId: String) {
        launch {
            chapterReadDao.setChapterRead(ChapterRead(mangaId = mangaId, chapterId = chapterId))
        }
    }
}