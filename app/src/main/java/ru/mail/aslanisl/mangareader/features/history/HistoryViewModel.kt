package ru.mail.aslanisl.mangareader.features.history

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.db.dao.MangaReadDao
import ru.mail.aslanisl.mangareader.domain.MangaReadUseCase
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class HistoryViewModel(
    private val source: IMangaSource,
    private val mangaReadDao: MangaReadDao,
    private val mangaReadUseCase: MangaReadUseCase
) : BaseViewModel() {

    fun loadHistoryManga(): LiveData<UIData<List<Manga>>> {
        val liveData = getLoadingLiveData<List<Manga>>()
        launch {
            val history = mangaReadDao.getReadManga(source::class.java.name)
            val list = history.map { Manga(it.id, it.photoUrl, it.name, it.description) }
            liveData.postValue(UIData.success(list))
        }
        return liveData
    }

    fun setMangaRead(manga: Manga) {
        launch {
            mangaReadUseCase.setMangaRead(manga, source::class.java.name)
        }
    }
}