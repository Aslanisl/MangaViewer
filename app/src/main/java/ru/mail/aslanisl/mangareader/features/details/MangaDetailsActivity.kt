package ru.mail.aslanisl.mangareader.features.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manga_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.features.base.BaseActivity
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.features.view.ChapterActivity

private const val KEY_MANGA = "KEY_MANGA"
private const val REQUEST_CODE_CHAPTER = 1

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

    private var selectedMangaPosition: Int = 0

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
        adapter.listener = { chapter, position ->
            ChapterActivity.openChapter(this, chapter.id, REQUEST_CODE_CHAPTER)
            viewModel.setChapterRead(mangaId, chapter.id)
            selectedMangaPosition = position
        }

        viewModel.loadChapters(mangaId).observe(this, observer)
    }

    private fun initData(data: UIData<List<Chapter>>) {
        data.body?.let { initChapters(it) }
    }

    private fun initChapters(chapters: List<Chapter>) {
        adapter.updateChapter(chapters)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHAPTER && resultCode == Activity.RESULT_OK) {
            adapter.selectPosition(selectedMangaPosition + 1)
        }
    }
}
