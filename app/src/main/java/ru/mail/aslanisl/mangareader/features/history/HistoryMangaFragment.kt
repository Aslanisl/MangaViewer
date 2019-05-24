package ru.mail.aslanisl.mangareader.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.features.base.BaseFragment
import ru.mail.aslanisl.mangareader.features.details.MangaDetailsActivity
import ru.mail.aslanisl.mangareader.features.mangaList.MangaAdapter

class HistoryMangaFragment : BaseFragment() {

    companion object {
        val TAG = HistoryMangaFragment::class.java.simpleName

        fun newInstance(): HistoryMangaFragment {
            return HistoryMangaFragment()
        }
    }

    private val viewModel: HistoryViewModel by viewModel()
    private val observer by lazy {
        Observer<UIData<List<Manga>>> {
            it ?: return@Observer
            initResult(it)
        }
    }

    private val adapter by lazy { MangaAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.enableLoading = false

        historyMangaList.layoutManager = LinearLayoutManager(contextNotNull)
        historyMangaList.adapter = adapter

        adapter.listener = { mangaInfo ->
            viewModel.setMangaRead(mangaInfo)
            MangaDetailsActivity.openManga(contextNotNull, mangaInfo.id)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadHistoryManga().observe(this, observer)
    }

    private fun initResult(data: UIData<List<Manga>>) {
        data.body?.let { updateMangas(it) }
    }

    private fun updateMangas(mangases: List<Manga>) {
        adapter.updateItems(mangases)
    }
}