package ru.mail.aslanisl.mangareader.features.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_chapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.features.base.BaseActivity
import ru.mail.aslanisl.mangareader.getDrawableCompat
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.isVisible
import ru.mail.aslanisl.mangareader.readStringAssets
import ru.mail.aslanisl.mangareader.show

private const val KEY_CHAPTER = "KEY_CHAPTER"
private const val UI_ANIMATION_DURATION = 300L

private const val KEY_IMAGES = "{IMAGES}"

class ChapterActivity : BaseActivity() {

    private val viewModel: ChapterViewModel by viewModel()
    private val observer by lazy {
        Observer<UIData<List<Page>>> {
            it ?: return@Observer
            initData(it)
        }
    }

    companion object {
        fun openChapter(activity: BaseActivity, chapterId: String, requestCode: Int) {
            val intent = Intent(activity, ChapterActivity::class.java)
            intent.putExtra(KEY_CHAPTER, chapterId)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var animating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val chapterId = intent?.getStringExtra(KEY_CHAPTER)
        if (chapterId.isNullOrEmpty()) {
            finish()
            return
        }

        initViews()

        viewModel.loadPages(chapterId).observe(this, observer)
    }

    private fun initViews() {
        chapterToolbar.gone()
        chapterToolbar.navigationIcon = getDrawableCompat(R.drawable.ic_left_arrow)
        chapterToolbar.setNavigationOnClickListener { onBackPressed() }

        webView.setOnClickListener { toggleUI() }
        webView.settings.domStorageEnabled = true

        val dir = cacheDir
        if (!dir.exists()) {
            dir.mkdirs()
        }
        webView.settings.setAppCachePath(dir.path)
        webView.settings.allowFileAccess = true
        webView.settings.setAppCacheEnabled(true)

        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
    }

    private fun toggleUI() {
        if (animating) return
        if (chapterToolbar.isVisible()) {
            chapterToolbar
                .animate()
                .translationY(-chapterToolbar.height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        animating = false
                        chapterToolbar.gone()
                    }
                })
                .duration = UI_ANIMATION_DURATION
        } else {
            chapterToolbar.show()
            chapterToolbar
                .animate()
                .translationY(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        animating = false
                    }
                })
                .duration = UI_ANIMATION_DURATION
        }
    }

    private fun initData(data: UIData<List<Page>>) {
        data.body?.let { showPages(it) }
    }

    private fun showPages(pages: List<Page>) {
        var imgElements = ""

        pages.forEach {
            imgElements += "<img src='" + it.imageUrl + "'/>"
        }
        val html = readStringAssets("Pages.html").replace(KEY_IMAGES, imgElements)

        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
    }
}
