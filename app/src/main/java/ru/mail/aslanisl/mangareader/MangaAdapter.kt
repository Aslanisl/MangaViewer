package ru.mail.aslanisl.mangareader

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.mail.aslanisl.mangareader.MangaAdapter.MangaViewHolder

class MangaAdapter : RecyclerView.Adapter<MangaViewHolder>() {

    private val mangasList = mutableListOf<MangaInfo>()

    var listener: ((MangaInfo) -> Unit)? = null

    fun updateMangas(mangas: List<MangaInfo>) {
        mangasList.clear()
        mangasList.addAll(mangas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MangaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_manga, parent, false)
        return MangaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.init(mangasList[position])
    }

    override fun getItemCount() = mangasList.size

    inner class MangaViewHolder(itemView: View) : ViewHolder(itemView) {
        private val photo = itemView.findViewById<ImageView>(R.id.mangaPhoto)
        private val name = itemView.findViewById<TextView>(R.id.mangaName)
        private val description = itemView.findViewById<TextView>(R.id.mangaDescription)

        private var currentManga: MangaInfo? = null

        init {
            itemView.setOnClickListener {
                val manga = currentManga ?: return@setOnClickListener
                listener?.invoke(manga)
            }
        }

        fun init(mangaInfo: MangaInfo) {
            currentManga = mangaInfo
            Glide.with(photo).load(mangaInfo.photoUrl).into(photo)
            name.text = mangaInfo.name
            description.text = mangaInfo.description
        }
    }
}