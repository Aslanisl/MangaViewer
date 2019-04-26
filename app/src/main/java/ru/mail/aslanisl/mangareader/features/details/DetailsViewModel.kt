package ru.mail.aslanisl.mangareader.features.details

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.BaseViewModel
import ru.mail.aslanisl.mangareader.dataModel.Chapter
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class DetailsViewModel constructor(private val source: IMangaSource): BaseViewModel() {

    fun loadChapters(idManga: String): LiveData<UIData<List<Chapter>>> {
        val liveData = getLoadingLiveData<List<Chapter>>()
        launch {
            liveData.postValue(source.loadChapter(idManga))
        }
        return liveData
    }
}