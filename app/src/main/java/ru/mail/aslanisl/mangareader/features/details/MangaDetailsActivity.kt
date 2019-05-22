package ru.mail.aslanisl.mangareader.features.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manga_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.data.model.MangaDetails
import ru.mail.aslanisl.mangareader.features.base.BaseActivity
import ru.mail.aslanisl.mangareader.features.view.ChapterActivity
import ru.mail.aslanisl.mangareader.getDrawableCompat
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.show
import ru.mail.aslanisl.mangareader.toast
import ru.mail.aslanisl.mangareader.utils.image.ImageLoadService


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
        Observer<UIData<MangaDetails>> {
            it ?: return@Observer
            initData(it)
        }
    }

    private var selectedMangaPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_details)

        val mangaId = intent?.getStringExtra(KEY_MANGA)
        if (mangaId.isNullOrEmpty()) {
            finish()
            return
        }

        initViews(mangaId)

        viewModel.loadChapters(mangaId).observe(this, observer)
    }

    private fun initViews(mangaId: String) {
        val navigationIcon = getDrawableCompat(R.drawable.ic_left_arrow, Color.BLACK)
        mangaDetailsToolbar.navigationIcon = navigationIcon
        mangaDetailsToolbar.setNavigationOnClickListener { onBackPressed() }

        chapterList.layoutManager = LinearLayoutManager(this)
        chapterList.adapter = adapter
        adapter.listener = { chapter, position ->
            ChapterActivity.openChapter(this, chapter.id, REQUEST_CODE_CHAPTER)
            viewModel.setChapterRead(mangaId, chapter.id)
            selectedMangaPosition = position
        }
    }

    private fun initData(data: UIData<MangaDetails>) {
        when {
            data.isSuccess() -> data.body?.let { initMangaDetails(it) }
            data.isError() -> toast(data.throwable?.message)
        }
    }

    private fun initMangaDetails(mangaDetails: MangaDetails) {
        mangaDetailsToolbar.title = mangaDetails.name
        ImageLoadService.loadUrl(mangaDetails.photoUrl, mangaPoster)
        if (mangaDetails.photoUrl.isNullOrEmpty()) mangaPoster.gone() else mangaPoster.show()
        description.text = mangaDetails.description
        if (mangaDetails.description.isEmpty()) description.gone() else description.show()
        mangaGenres.text = mangaDetails.genre.joinToString()
        if (mangaDetails.genre.isEmpty()) mangaGenres.gone() else mangaGenres.show()
        initChapters(mangaDetails.chapters)
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
