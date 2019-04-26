package ru.mail.aslanisl.mangareader.features.search

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.BaseActivity
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.dataModel.Manga
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.features.details.MangaDetailsActivity

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    private val observer by lazy {
        Observer<UIData<List<Manga>>> {
            it ?: return@Observer
            initResult(it)
        }
    }

    private val mangaAdapter by lazy { MangaAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        search.setOnClickListener { search() }

        mangasList.layoutManager = LinearLayoutManager(this)
        mangasList.adapter = mangaAdapter

        mangaAdapter.listener = { mangaInfo -> MangaDetailsActivity.openManga(this, mangaInfo.id) }
    }

    private fun search() {
        viewModel.search(searchEdit.text.toString()).observe(this, observer)
    }

    private fun initResult(data: UIData<List<Manga>>) {
        data.body?.let { updateMangas(it) }
    }

    private fun updateMangas(mangases: List<Manga>) {
        mangaAdapter.updateMangas(mangases)
    }
}
