package ru.mail.aslanisl.mangareader.features.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chapter.*
import ru.mail.aslanisl.mangareader.BaseActivity
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.dataModel.Page
import ru.mail.aslanisl.mangareader.dataModel.base.UIData
import ru.mail.aslanisl.mangareader.di.component.AppComponent

private const val KEY_CHAPTER = "KEY_CHAPTER"

class ChapterActivity : BaseActivity() {

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    private val adapter by lazy { PageAdapter() }
    private val viewModel by lazy { initViewModel<ChapterViewModel>() }
    private val observer by lazy {
        Observer<UIData<List<Page>>> {
            it ?: return@Observer
            initData(it)
        }
    }

    companion object {
        fun openChapter(context: Context, chapterId: String) {
            val intent = Intent(context, ChapterActivity::class.java)
            intent.putExtra(KEY_CHAPTER, chapterId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_chapter)

        val chapterId = intent?.getStringExtra(KEY_CHAPTER)
        if (chapterId.isNullOrEmpty()) {
            finish()
            return
        }

        chapterImages.layoutManager = LinearLayoutManager(this)
        chapterImages.adapter = adapter

        viewModel.loadPages(chapterId).observe(this, observer)
    }

    private fun initData(data: UIData<List<Page>>) {
        data.body?.let { showPages(it) }
    }

    private fun showPages(pages: List<Page>) {
        adapter.updatePages(pages)
    }
}
