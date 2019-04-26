package ru.mail.aslanisl.mangareader.features.search

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.BaseViewModel
import ru.mail.aslanisl.mangareader.dataModel.Manga
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class MainViewModel constructor(private val source: IMangaSource): BaseViewModel() {

    fun search(term: String): LiveData<UIData<List<Manga>>> {
        val liveData = getLoadingLiveData<List<Manga>>()
        launch {
            liveData.postValue(source.searchManga(term))
        }
        return liveData
    }
}