package ru.mail.aslanisl.mangareader.features.mangaList

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.domain.MangaReadUseCase
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class MainViewModel constructor(
    private val source: IMangaSource,
    private val mangaReadUseCase: MangaReadUseCase
) : BaseViewModel() {

    fun search(term: String): LiveData<UIData<List<Manga>>> {
        val liveData = getLoadingLiveData<List<Manga>>()
        launch {
            liveData.postValue(source.searchManga(term))
        }
        return liveData
    }

    fun setMangaRead(manga: Manga) {
        launch {
            mangaReadUseCase.setMangaRead(manga, source::class.java.name)
        }
    }
}