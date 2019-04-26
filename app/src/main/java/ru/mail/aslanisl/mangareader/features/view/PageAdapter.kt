package ru.mail.aslanisl.mangareader.features.view

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mail.aslanisl.mangareader.R.id
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.dataModel.Page
import ru.mail.aslanisl.mangareader.features.view.PageAdapter.ImageViewHolder

class PageAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<Page>()

    fun updatePages(imagesList: List<Page>) {
        images.clear()
        images.addAll(imagesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.init(images[position])
    }

    override fun getItemCount() = images.size

    inner class ImageViewHolder(itemView: View) : ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(id.image)

        fun init(image: Page) {
            Glide.with(imageView).load(image.imageUrl).into(imageView)
        }
    }
}