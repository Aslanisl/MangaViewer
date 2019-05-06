package ru.mail.aslanisl.mangareader.features.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.features.genre.GenreAdapter.GenreViewHolder

class GenreAdapter : RecyclerView.Adapter<GenreViewHolder>() {

    private val genres = mutableListOf<Genre>()

    var listener: ((Genre) -> Unit)? = null

    fun updateGenres(genres: List<Genre>) {
        this.genres.clear()
        this.genres.addAll(genres)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.init(genres[position])
    }

    override fun getItemCount() = genres.size

    inner class GenreViewHolder(itemView: View) : ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.genreTitle)
        private var currentGenre: Genre? = null

        init {
            itemView.setOnClickListener {
                val genre = currentGenre ?: return@setOnClickListener
                notifyItemChanged(adapterPosition)
                listener?.invoke(genre)
            }
        }

        fun init(genre: Genre) {
            currentGenre = genre
            name.text = genre.title
        }
    }
}