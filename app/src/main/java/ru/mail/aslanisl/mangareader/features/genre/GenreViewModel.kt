package ru.mail.aslanisl.mangareader.features.genre

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.domain.MangaReadUseCase
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel
import ru.mail.aslanisl.mangareader.getLoadingLiveData
import ru.mail.aslanisl.mangareader.source.IMangaSource

class GenreViewModel(
    private val source: IMangaSource,
    private val mangaReadUseCase: MangaReadUseCase
) : BaseViewModel() {

    fun loadGenres(): LiveData<UIData<List<Genre>>> {
        val liveData = getLoadingLiveData<List<Genre>>()
        launch {
            val genres = source.loadGenres()
            liveData.postValue(genres)
        }
        return liveData
    }

    fun loadMangaForGenre(genre: Genre): LiveData<UIData<List<Manga>>> {
        val liveData = getLoadingLiveData<List<Manga>>()
        launch {
            val mangas = source.loadMangaGenre(genre.id)
            liveData.postValue(mangas)
        }
        return liveData
    }

    fun loadMangaForGenre(genre: Genre, offset: Int): LiveData<UIData<List<Manga>>> {
        val liveData = getLoadingLiveData<List<Manga>>()
        launch {
            val mangas = source.loadMangaGenre(genre.id, offset)
            liveData.postValue(mangas)
        }
        return liveData
    }

    fun getGenrePagingCount() = source.genrePagingCount()

    fun setMangaRead(manga: Manga) {
        launch {
            mangaReadUseCase.setMangaRead(manga, source::class.java.name)
        }
    }
}