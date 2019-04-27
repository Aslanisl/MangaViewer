package ru.mail.aslanisl.mangareader.features.main

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.custom_nav_view.view.*
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.getColorCompat

class BottomNavView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val selectColor = context.getColorCompat(R.color.bottomSelectColor)
    private val unselectColor = context.getColorCompat(R.color.bottomUnselectColor)

    enum class Item {
        NONE,
        SEARCH,
        GENRE
    }

    var listener: ((Item) -> Unit)? = null

    var currentItem = Item.NONE

    init {
        inflate(context, R.layout.custom_nav_view, this)
        search.setOnClickListener { selectItem(Item.SEARCH) }
        genre.setOnClickListener { selectItem(Item.GENRE) }
    }

    fun selectItem(item: Item) {
        if (currentItem == item) return
        listener?.invoke(item)
        updateColorItem(search, item == Item.SEARCH)
        updateColorItem(genre, item == Item.GENRE)
    }

    private fun updateColorItem(view: ImageView, select: Boolean) {
        view.setColorFilter(if (select) selectColor else unselectColor, PorterDuff.Mode.OVERLAY)
    }
}