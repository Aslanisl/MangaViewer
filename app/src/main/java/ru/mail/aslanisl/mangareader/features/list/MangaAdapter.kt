package ru.mail.aslanisl.mangareader.features.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.mail.aslanisl.mangareader.R.id
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.features.list.MangaAdapter.MangaViewHolder

class MangaAdapter : RecyclerView.Adapter<MangaViewHolder>() {

    private val mangasList = mutableListOf<Manga>()

    var listener: ((Manga) -> Unit)? = null

    fun updateMangas(mangases: List<Manga>) {
        mangasList.clear()
        mangasList.addAll(mangases)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MangaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_manga, parent, false)
        return MangaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.init(mangasList[position])
    }

    override fun getItemCount() = mangasList.size

    inner class MangaViewHolder(itemView: View) : ViewHolder(itemView) {
        private val photo = itemView.findViewById<ImageView>(id.mangaPhoto)
        private val name = itemView.findViewById<TextView>(id.mangaName)
        private val description = itemView.findViewById<TextView>(id.mangaDescription)

        private var currentManga: Manga? = null

        init {
            itemView.setOnClickListener {
                val manga = currentManga ?: return@setOnClickListener
                listener?.invoke(manga)
            }
        }

        fun init(manga: Manga) {
            currentManga = manga
            Glide.with(photo).load(manga.photoUrl).into(photo)
            name.text = manga.name
            description.text = manga.description
        }
    }
}