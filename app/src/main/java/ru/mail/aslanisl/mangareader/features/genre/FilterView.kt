package ru.mail.aslanisl.mangareader.features.genre

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.custom_filter_view.view.*
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.getColorCompat
import ru.mail.aslanisl.mangareader.getDrawableCompat
import ru.mail.aslanisl.mangareader.getString
import ru.mail.aslanisl.mangareader.source.MangaFilter
import ru.mail.aslanisl.mangareader.source.MangaFilter.CHAPTER
import ru.mail.aslanisl.mangareader.source.MangaFilter.DATE
import ru.mail.aslanisl.mangareader.source.MangaFilter.FAVORITE
import ru.mail.aslanisl.mangareader.source.MangaFilter.NAME
import ru.mail.aslanisl.mangareader.source.MangaFilter.NONE

class FilterView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val textSelectedColor by lazy { context.getColorCompat(R.color.white) }
    private val textUnselectedColor by lazy { context.getColorCompat(R.color.orange) }

    private val bgSelected by lazy { context.getDrawableCompat(R.drawable.bg_search_button) }
    private val bgUnselected by lazy { context.getDrawableCompat(R.drawable.bg_search) }

    var filter: MangaFilter? = null
        private set

    var listener: ((filter: MangaFilter) -> Unit)? = null

    init {
        inflate(context, R.layout.custom_filter_view, this)

        super.setOnClickListener {
            val filter = filter ?: return@setOnClickListener
            filter.asc = filter.asc.not()

            filterArrow.rotation = if (filter.asc) 0f else 180f

            isSelected = true

            listener?.invoke(filter)
        }

        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.FilterView,
                0, 0
            )

            val position = typedArray.getInt(R.styleable.FilterView_filter, 0)
            try {
                filter = MangaFilter.values()[position]
                updateFilter()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            typedArray.recycle()
        }

        updateSelected(isSelected)
    }

    fun setFilter(filter: MangaFilter) {
        this.filter = filter
        updateFilter()
    }

    override fun setSelected(selected: Boolean) {
        if (selected == isSelected) return
        super.setSelected(selected)

        updateSelected(selected)
    }

    private fun updateSelected(selected: Boolean) {
        filterText.setTextColor(if (selected) textSelectedColor else textUnselectedColor)
        background = if (selected) bgSelected else bgUnselected

        filterArrow.visibility = if (selected) View.VISIBLE else View.GONE
    }

    private fun updateFilter() {
        val filter = filter ?: return

        filterText.text = when (filter) {
            NONE -> ""
            DATE -> getString(R.string.filter_date)
            FAVORITE -> getString(R.string.filter_favorite)
            NAME -> getString(R.string.filter_name)
            CHAPTER -> getString(R.string.filter_chapters)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        // Not supporting
    }
}