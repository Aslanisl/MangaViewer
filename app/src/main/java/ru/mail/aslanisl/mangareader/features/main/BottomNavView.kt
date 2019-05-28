package ru.mail.aslanisl.mangareader.features.main

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
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

    private val fontSizeSelected = context.getDimensionPixel(R.dimen.font_normal).toFloat()
    private val fontSizeUnselected = context.getDimensionPixel(R.dimen.font_small).toFloat()

    var listener: ((MainItem) -> Unit)? = null
    var sameItemListener: ((MainItem) -> Unit)? = null

    private fun getDefaultValueAnimator(): ValueAnimator {
        return ValueAnimator().apply { duration = DURATION_ANIMATION }
    }

    private val items: List<BottomViewItem>

    var currentItem = MainItem.NONE

    init {
        inflate(context, R.layout.custom_nav_view, this)
        search.setOnClickListener { selectItem(MainItem.SEARCH) }
        genre.setOnClickListener { selectItem(MainItem.GENRE) }
        history.setOnClickListener { selectItem(MainItem.HISTORY) }
        settings.setOnClickListener { selectItem(MainItem.SETTINGS) }

        items = listOf(
            BottomViewItem(SEARCH, searchImage, searchTitle),
            BottomViewItem(GENRE, genreImage, genreTitle),
            BottomViewItem(HISTORY, historyImage, historyTitle),
            BottomViewItem(SETTINGS, settingsImage, settingsTitle)
        )

        updateItems(false)
    }

    fun selectItem(item: MainItem) {
        if (currentItem == item) {
            sameItemListener?.invoke(item)
            return
        }
        listener?.invoke(item)
        currentItem = item

        updateItems(true)
    }

    private fun updateItems(animate: Boolean = true) {
        items.forEach {
            val select = it.item == currentItem
            it.valueAnimator.cancel()

            if (animate.not()) {
                it.imageView.setColorFilter(if (select) selectColor else unselectColor, PorterDuff.Mode.MULTIPLY)

                it.imageView.layoutParams.height = if (select) iconSizeSelected else iconSizeUnselected
                it.imageView.layoutParams.width = if (select) iconSizeSelected else iconSizeUnselected
                it.imageView.requestLayout()

                val size = if (select) fontSizeSelected else fontSizeUnselected
                it.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)

            } else {
                val old = it.valueAnimator.animatedValue as? Float
                val from = old ?: if (select) 0f else 1f
                val to = if (select) 1f else 0f

                if (from == to) return@forEach

                it.valueAnimator.setFloatValues(from, to)
                it.valueAnimator.start()
            }
        }
    }

    inner class BottomViewItem(
        val item: MainItem,
        val imageView: ImageView,
        val textView: TextView,
        val valueAnimator: ValueAnimator = getDefaultValueAnimator()
    ) {
        init {
            valueAnimator.addUpdateListener {
                val value = it.animatedValue as Float

                val ratio = 1f / value
                val diff = (iconSizeSelected - iconSizeUnselected) / ratio
                imageView.layoutParams.height = (iconSizeUnselected + diff).roundToInt()
                imageView.layoutParams.width = (iconSizeUnselected + diff).roundToInt()
                imageView.requestLayout()

                val diffText = (fontSizeSelected - fontSizeUnselected) / ratio
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeUnselected + diffText)

                val color = ColorUtils.blendARGB(unselectColor, selectColor, value)
                imageView.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            }
        }
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
        updateItems(false)

        super.onRestoreInstanceState(superState)
    }
}