package ru.mail.aslanisl.mangareader.features.view

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class ChapterViewModel constructor(private val source: IMangaSource): BaseViewModel() {

    fun loadPages(chapterId: String): LiveData<UIData<List<Page>>> {
        val liveData = getLoadingLiveData<List<Page>>()
        launch {
            liveData.postValue(source.loadPages(chapterId))
        }
        return liveData
    }
}