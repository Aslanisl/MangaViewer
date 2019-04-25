package ru.mail.aslanisl.mangareader

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.mail.aslanisl.mangareader.ImageAdapter.ImageViewHolder

class ImageAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<String>()

    fun updateImages(imagesList: List<String>) {
        images.clear()
        images.addAll(imagesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.init(images[position])
    }

    override fun getItemCount() = images.size

    inner class ImageViewHolder(itemView: View) : ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.image)

        fun init(image: String) {
            Glide.with(imageView).load(image).into(imageView)
        }
    }
}