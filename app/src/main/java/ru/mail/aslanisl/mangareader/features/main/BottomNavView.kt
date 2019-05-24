package ru.mail.aslanisl.mangareader.features.main

import android.content.Context
import android.graphics.PorterDuff
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.custom_nav_view.view.*
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.getColorCompat
import android.os.Bundle

private const val KEY_SUPER_BUNDLE = "KEY_SUPER_BUNDLE"
private const val KEY_CURRENT_ITEM = "KEY_CURRENT_ITEM"

class BottomNavView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val selectColor = context.getColorCompat(R.color.bottomSelectColor)
    private val unselectColor = context.getColorCompat(R.color.bottomUnselectColor)

    var listener: ((MainItem) -> Unit)? = null

    var currentItem = MainItem.NONE

    init {
        inflate(context, R.layout.custom_nav_view, this)
        search.setOnClickListener { selectItem(MainItem.SEARCH) }
        genre.setOnClickListener { selectItem(MainItem.GENRE) }
        history.setOnClickListener { selectItem(MainItem.HISTORY) }
    }

    fun selectItem(item: MainItem) {
        if (currentItem == item) return
        listener?.invoke(item)
        currentItem = item
        updateItems()
    }

    private fun updateItems() {
        updateColorItem(search, currentItem == MainItem.SEARCH)
        updateColorItem(genre, currentItem == MainItem.GENRE)
        updateColorItem(history, currentItem == MainItem.HISTORY)
    }

    private fun updateColorItem(view: ImageView, select: Boolean) {
        view.setColorFilter(if (select) selectColor else unselectColor, PorterDuff.Mode.MULTIPLY)
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