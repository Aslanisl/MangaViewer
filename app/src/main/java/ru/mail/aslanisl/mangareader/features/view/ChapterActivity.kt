package ru.mail.aslanisl.mangareader.features.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.features.base.BaseActivity
import ru.mail.aslanisl.mangareader.getDrawableCompat
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.isVisible
import ru.mail.aslanisl.mangareader.show

private const val KEY_CHAPTER = "KEY_CHAPTER"
private const val UI_ANIMATION_DURATION = 300L

class ChapterActivity : BaseActivity() {

    private val adapter by lazy { PageAdapter() }

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

    private var pageCount = 0

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

        val lm = object : LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {
            // Load more 2 items
            override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
                return 2
            }
        }
        chapterImages.layoutManager = lm
        chapterImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return
                }
                val firstVisible = lm.findFirstCompletelyVisibleItemPosition()
                if (firstVisible == RecyclerView.NO_POSITION) {
                    return
                }
                updateToolbarTitle(firstVisible + 1)
            }
        })
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(chapterImages)
        chapterImages.adapter = adapter

        adapter.tapListener = { toggleUI() }

        previous.setOnClickListener(::previous)
        next.setOnClickListener(::next)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                previous()
                true
            }

            KeyEvent.KEYCODE_VOLUME_UP -> {
                next()
                true
            }

            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun next(view: View? = null) {
        val lm = chapterImages.layoutManager as LinearLayoutManager
        val currentPosition = lm.findFirstCompletelyVisibleItemPosition()
        if (currentPosition < 0) return
        if (currentPosition + 1 >= adapter.itemCount) {
            setResult(Activity.RESULT_OK)
            finish()
            return
        }
        chapterImages.smoothScrollToPosition(currentPosition + 1)
    }

    private fun previous(view: View? = null) {
        val lm = chapterImages.layoutManager as LinearLayoutManager
        val currentPosition = lm.findFirstCompletelyVisibleItemPosition()
        if (currentPosition <= 0) return
        chapterImages.smoothScrollToPosition(currentPosition - 1)
    }

    private var animating = false

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
        adapter.updatePages(pages)
        pageCount = pages.size
        updateToolbarTitle(1)
    }

    private fun updateToolbarTitle(currentPosition: Int) {
        chapterToolbar.title = "$currentPosition/$pageCount"
    }
}
