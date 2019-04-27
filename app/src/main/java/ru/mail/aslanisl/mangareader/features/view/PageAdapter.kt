package ru.mail.aslanisl.mangareader.features.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import ru.mail.aslanisl.mangareader.R.id
import ru.mail.aslanisl.mangareader.R.layout
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.features.view.PageAdapter.ImageViewHolder

class PageAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<Page>()

    var tapListener: (() -> Unit?)? = null

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
        private val imageView = itemView.findViewById<PhotoView>(id.image)

        init {
            imageView.setOnPhotoTapListener { _, _, _ ->
                tapListener?.invoke()
            }
        }

        fun init(image: Page) {
            Glide.with(imageView).load(image.imageUrl).into(imageView)
        }
    }
}