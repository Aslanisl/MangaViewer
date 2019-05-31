package ru.mail.aslanisl.mangareader.features.genre

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.custom_filter_container.view.*
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.source.MangaFilter
import ru.mail.aslanisl.mangareader.source.MangaFilter.NONE

class FilterContainer
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val filters by lazy {
        listOf(filterDate, filterFavorite, filterName, filterChapter)
    }

    var currentFilter = MangaFilter.NONE
        private set

    var listener: ((filter: MangaFilter) -> Unit)? = null

    init {
        inflate(context, R.layout.custom_filter_container, this)

        filters.forEach { view ->
            view.listener = { selectFilter(it, true) }
        }
    }

    fun selectFilter(filter: MangaFilter, toggleListener: Boolean = true) {
        filters.forEach {
            it.isSelected = it.filter == filter
        }

        currentFilter = filter
        if (toggleListener) listener?.invoke(filter)
    }

    fun clearFilter() {
        currentFilter = NONE
        filters.forEach { it.isSelected = false }
    }
}