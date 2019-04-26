package ru.mail.aslanisl.mangareader.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.mail.aslanisl.mangareader.di.factory.ViewModelFactory
import ru.mail.aslanisl.mangareader.di.factory.ViewModelKey
import ru.mail.aslanisl.mangareader.features.details.DetailsViewModel
import ru.mail.aslanisl.mangareader.features.search.MainViewModel
import ru.mail.aslanisl.mangareader.features.view.ChapterViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    internal abstract fun detailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChapterViewModel::class)
    internal abstract fun chapterViewModel(viewModel: ChapterViewModel): ViewModel
}