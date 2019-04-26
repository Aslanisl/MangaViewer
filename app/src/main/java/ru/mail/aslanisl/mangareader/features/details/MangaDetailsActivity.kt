package ru.mail.aslanisl.mangareader.features.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manga_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.BaseActivity
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.dataModel.Chapter
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.features.view.ChapterActivity

private const val KEY_MANGA = "KEY_MANGA"

class MangaDetailsActivity : BaseActivity() {

    companion object {
        fun openManga(context: Context, id: String) {
            val intent = Intent(context, MangaDetailsActivity::class.java)
            intent.putExtra(KEY_MANGA, id)
            context.startActivity(intent)
        }
    }

    private val adapter by lazy { ChapterAdapter() }
    private val viewModel: DetailsViewModel by viewModel()
    private val observer by lazy {
        Observer<UIData<List<Chapter>>> {
            it ?: return@Observer
            initData(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_manga_details)

        val mangaId = intent?.getStringExtra(KEY_MANGA)
        if (mangaId.isNullOrEmpty()) {
            finish()
            return
        }

        chapterList.layoutManager = LinearLayoutManager(this)
        chapterList.adapter = adapter
        adapter.listener = { ChapterActivity.openChapter(this, it.id) }

        viewModel.loadChapters(mangaId).observe(this, observer)
    }

    private fun initData(data: UIData<List<Chapter>>) {
        data.body?.let { initChapters(it) }
    }

    private fun initChapters(chapters: List<Chapter>) {
        adapter.updateChapter(chapters)
    }
}
