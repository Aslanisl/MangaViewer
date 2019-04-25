package ru.mail.aslanisl.mangareader

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.mail.aslanisl.mangareader.ChapterAdapter.ChapterViewHolder

class ChapterAdapter : RecyclerView.Adapter<ChapterViewHolder>() {

    private val chapters = mutableListOf<Chapter>()

    var listener: ((Chapter) -> Unit)? = null

    fun updateChapter(chapters: List<Chapter>) {
        this.chapters.clear()
        this.chapters.addAll(chapters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.init(chapters[position])
    }

    override fun getItemCount() = chapters.size

    inner class ChapterViewHolder(itemView: View) : ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.chapterName)
        private var currentChapter: Chapter? = null

        init {
            itemView.setOnClickListener {
                val chapter = currentChapter ?: return@setOnClickListener
                listener?.invoke(chapter)
            }
        }

        fun init(chapter: Chapter) {
            currentChapter = chapter
            name.text = chapter.title
        }
    }
}