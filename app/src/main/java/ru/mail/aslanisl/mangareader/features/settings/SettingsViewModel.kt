package ru.mail.aslanisl.mangareader.features.settings

import kotlinx.coroutines.launch
import ru.mail.aslanisl.mangareader.domain.MangaReadUseCase
import ru.mail.aslanisl.mangareader.features.base.BaseViewModel

class SettingsViewModel(private val mangaReadUseCase: MangaReadUseCase) : BaseViewModel() {

    fun clearHistory() {
        launch {
            mangaReadUseCase.clearMangaHistory()
        }
    }
}