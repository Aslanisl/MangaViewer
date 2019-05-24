package ru.mail.aslanisl.mangareader.features.main

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_nav_view.view.*
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.features.main.MainItem.GENRE
import ru.mail.aslanisl.mangareader.features.main.MainItem.HISTORY
import ru.mail.aslanisl.mangareader.features.main.MainItem.SEARCH
import ru.mail.aslanisl.mangareader.features.main.MainItem.SETTINGS
import ru.mail.aslanisl.mangareader.getColorCompat
import ru.mail.aslanisl.mangareader.getDimensionPixel
import kotlin.math.roundToInt

private const val KEY_SUPER_BUNDLE = "KEY_SUPER_BUNDLE"
private const val KEY_CURRENT_ITEM = "KEY_CURRENT_ITEM"

private const val DURATION_ANIMATION = 150L

class BottomNavView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val selectColor = context.getColorCompat(R.color.bottomSelectColor)
    private val unselectColor = context.getColorCompat(R.color.bottomUnselectColor)

    private val iconSizeSelected = context.getDimensionPixel(R.dimen.bottom_item_image_height_selected)
    private val iconSizeUnselected = context.getDimensionPixel(R.dimen.bottom_item_image_height_unselected)

    private val fontSizeSelected = context.getDimensionPixel(R.dimen.font_normal)
    private val fontSizeUnselected = context.getDimensionPixel(R.dimen.font_small)

    private val animators = mutableMapOf<Int, ValueAnimator>()

    var listener: ((MainItem) -> Unit)? = null
    var sameItemListener: ((MainItem) -> Unit)? = null

    var currentItem = MainItem.NONE

    init {
        inflate(context, R.layout.custom_nav_view, this)
        search.setOnClickListener { selectItem(MainItem.SEARCH) }
        genre.setOnClickListener { selectItem(MainItem.GENRE) }
        history.setOnClickListener { selectItem(MainItem.HISTORY) }
        settings.setOnClickListener { selectItem(MainItem.SETTINGS) }
    }

    fun selectItem(item: MainItem) {
        if (currentItem == item) {
            sameItemListener?.invoke(item)
            return
        }
        listener?.invoke(item)
        currentItem = item
        updateItems()
    }

    private fun updateItems() {
        updateColorItem(searchImage, currentItem == MainItem.SEARCH)
        updateColorItem(genreImage, currentItem == MainItem.GENRE)
        updateColorItem(historyImage, currentItem == MainItem.HISTORY)
        updateColorItem(settingsImage, currentItem == MainItem.SETTINGS)

        updateTitleItem(searchTitle, currentItem == MainItem.SEARCH)
        updateTitleItem(genreTitle, currentItem == MainItem.GENRE)
        updateTitleItem(historyTitle, currentItem == MainItem.HISTORY)
        updateTitleItem(settingsTitle, currentItem == MainItem.SETTINGS)

        selectItem()
    }

    private fun selectItem() {
        val image = when (currentItem) {
            SEARCH -> searchImage
            GENRE -> genreImage
            HISTORY -> historyImage
            SETTINGS -> settingsImage
            else -> null
        } ?: return

        val title = when (currentItem) {
            SEARCH -> searchTitle
            GENRE -> genreTitle
            HISTORY -> historyTitle
            SETTINGS -> settingsTitle
            else -> null
        } ?: return

        var animator = animators[image.id]
        if (animator == null) {
            animator = ValueAnimator()
            animator.duration = DURATION_ANIMATION
            animators[image.id] = animator
        } else {
            animator.cancel()
            animator.removeAllUpdateListeners()
        }
        animator.setFloatValues(0f, 1f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float

            val ratio = 1f / value
            val diff = (iconSizeSelected - iconSizeUnselected) / ratio
            image.layoutParams.height = (iconSizeUnselected + diff).roundToInt()
            image.layoutParams.width = (iconSizeUnselected + diff).roundToInt()
            image.requestLayout()
        }
        animator.start()
    }

    private fun updateColorItem(view: ImageView, select: Boolean) {
        view.setColorFilter(if (select) selectColor else unselectColor, PorterDuff.Mode.MULTIPLY)
    }

    private fun updateTitleItem(view: TextView, select: Boolean) {
        view.setTypeface(null, if (select) Typeface.BOLD else Typeface.NORMAL)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_BUNDLE, super.onSaveInstanceState())
        bundle.putString(KEY_CURRENT_ITEM, currentItem.name)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        if (state is Bundle) {
            currentItem = try {
                MainItem.valueOf(state.getString(KEY_CURRENT_ITEM) ?: "")
            } catch (e: Exception) {
                MainItem.SEARCH
            }
            superState = state.getParcelable(KEY_SUPER_BUNDLE)
        }
        updateItems()

        super.onRestoreInstanceState(superState)
    }
}