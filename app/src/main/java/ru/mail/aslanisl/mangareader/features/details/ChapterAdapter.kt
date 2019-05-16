package ru.mail.aslanisl.mangareader.features.details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.mail.aslanisl.mangareader.R.id
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.data.model.Chapter
import ru.mail.aslanisl.mangareader.features.details.ChapterAdapter.ChapterViewHolder

class ChapterAdapter : RecyclerView.Adapter<ChapterViewHolder>() {

    private val chapters = mutableListOf<Chapter>()

    var listener: ((Chapter, Int) -> Unit)? = null

    fun updateChapter(chapters: List<Chapter>) {
        this.chapters.clear()
        this.chapters.addAll(chapters)
        notifyDataSetChanged()
    }

    fun selectPosition(position: Int) {
        if (position >= chapters.size || position < 0) return
        val chapter = chapters[position]
        selectChapter(chapter, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.init(chapters[position])
    }

    override fun getItemCount() = chapters.size

    private fun selectChapter(chapter: Chapter, position: Int) {
        chapter.readed = true
        notifyItemChanged(position)
        listener?.invoke(chapter, position)
    }

    inner class ChapterViewHolder(itemView: View) : ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(id.chapterName)
        private var currentChapter: Chapter? = null

        init {
            itemView.setOnClickListener {
                val chapter = currentChapter ?: return@setOnClickListener
                selectChapter(chapter, adapterPosition)
            }
        }

        fun init(chapter: Chapter) {
            currentChapter = chapter
            name.text = chapter.title
            name.setTextColor(if (chapter.readed) Color.GRAY else Color.BLACK)
        }
    }
}