package ru.mail.aslanisl.mangareader.features.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_manga_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.features.base.BaseFragment
import ru.mail.aslanisl.mangareader.features.details.MangaDetailsActivity
import ru.mail.aslanisl.mangareader.features.search.MainViewModel

class MangaListFragment : BaseFragment() {

    companion object {
        val TAG = MangaListFragment::class.java.simpleName

        fun newInstance(): MangaListFragment {
            return MangaListFragment()
        }
    }

    private val viewModel: MainViewModel by viewModel()
    private val observer by lazy {
        Observer<UIData<List<Manga>>> {
            it ?: return@Observer
            initResult(it)
        }
    }

    private val mangaAdapter by lazy { MangaAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout.fragment_manga_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search.setOnClickListener { search(searchEdit.text.toString()) }

        mangasList.layoutManager = LinearLayoutManager(contextNotNull)
        mangasList.adapter = mangaAdapter

        mangaAdapter.listener = { mangaInfo -> MangaDetailsActivity.openManga(contextNotNull, mangaInfo.id) }
    }

    fun search(term: String) {
        viewModel.search(term).observe(this, observer)
    }

    private fun initResult(data: UIData<List<Manga>>) {
        data.body?.let { updateMangas(it) }
    }

    private fun updateMangas(mangases: List<Manga>) {
        mangaAdapter.updateMangas(mangases)
    }
}
