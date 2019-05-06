package ru.mail.aslanisl.mangareader.features.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_genre_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.features.base.BaseFragment
import ru.mail.aslanisl.mangareader.features.details.MangaDetailsActivity
import ru.mail.aslanisl.mangareader.features.mangaList.MangaAdapter
import ru.mail.aslanisl.mangareader.features.main.OnBackPressListener
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.isVisible
import ru.mail.aslanisl.mangareader.show

class GenreListFragment : BaseFragment(), OnBackPressListener {

    companion object {
        val TAG = GenreListFragment::class.java.simpleName

        fun newInstance(): GenreListFragment {
            return GenreListFragment()
        }
    }

    private val viewModel: GenreViewModel by viewModel()
    private val genreObserver by lazy {
        Observer<UIData<List<Genre>>> {
            it ?: return@Observer
            initGenreResult(it)
        }
    }
    private val mangaObserver by lazy {
        Observer<UIData<List<Manga>>> {
            it ?: return@Observer
            initMangaResult(it)
        }
    }
    private val mangaMoreObserver by lazy {
        Observer<UIData<List<Manga>>> {
            it ?: return@Observer
            initMangaMoreResult(it)
        }
    }

    private val genreAdapter by lazy { GenreAdapter() }
    private val mangaAdapter by lazy { MangaAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_genre_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreList.layoutManager = LinearLayoutManager(contextNotNull)
        genreList.adapter = genreAdapter

        mangasList.layoutManager = LinearLayoutManager(contextNotNull)
        mangasList.adapter = mangaAdapter
        mangaAdapter.visibleThreshold = viewModel.getGenrePagingCount()

        var currentGenre: Genre? = null

        if (genreAdapter.itemCount == 0) {
            viewModel.loadGenres().observe(this, genreObserver)
        }

        genreAdapter.listener = { genre ->
            genreList.gone()
            mangaAdapter.clearItems()
            currentGenre = genre
            viewModel.loadMangaForGenre(genre).observe(this, mangaObserver)
        }

        mangaAdapter.listener = { mangaInfo ->
            viewModel.setMangaRead(mangaInfo)
            MangaDetailsActivity.openManga(contextNotNull, mangaInfo.id)
        }

        mangaAdapter.setLoadMore {
            val genre = currentGenre ?: return@setLoadMore
            viewModel.loadMangaForGenre(genre, mangaAdapter.itemCount).observe(this, mangaMoreObserver)
        }
    }

    private fun initGenreResult(data: UIData<List<Genre>>) {
        data.body?.let { updateGenres(it) }
    }

    private fun updateGenres(genres: List<Genre>) {
        genreAdapter.updateGenres(genres)
    }

    private fun initMangaResult(data: UIData<List<Manga>>) {
        data.body?.let { updateMangaList(it) }
    }

    private fun updateMangaList(list: List<Manga>) {
        mangasList.show()
        mangaAdapter.updateItems(list)
    }

    private fun initMangaMoreResult(data: UIData<List<Manga>>) {
        val list = data.body ?: emptyList()
        updateMangaMoreList(list)
    }

    private fun updateMangaMoreList(list: List<Manga>) {
        mangaAdapter.addItems(list)
    }

    override fun onBackPressed(): Boolean {
        if (mangasList.isVisible()) {
            mangasList.gone()
            genreList.show()
            return true
        }
        return false
    }
}