package ru.mail.aslanisl.mangareader.features.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.chrisbanes.photoview.PhotoView
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.features.view.PageAdapter.ImageViewHolder
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.show
import ru.mail.aslanisl.mangareader.utils.image.ImageLoader
import ru.mail.aslanisl.mangareader.utils.image.NetProgressListener
import com.bumptech.glide.request.RequestListener as RequestListener1

class PageAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<Page>()

    var tapListener: (() -> Unit?)? = null

    fun updatePages(imagesList: List<Page>) {
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
        private val imageView = itemView.findViewById<PhotoView>(R.id.image)
        private val progressView = itemView.findViewById<ProgressBar>(R.id.progress)

        private val progressListener by lazy {
            object : NetProgressListener {
                override fun onStart() {
                    imageView.gone()
                    progressView.show()
                }

                override fun progressChanged(progress: Float) {
                    progressView.progress = progress.toInt()
                }

                override fun onFinish() {
                    imageView.show()
                    progressView.gone()
                }

                override fun onCancel() {
                    imageView.show()
                    progressView.gone()
                }

                override fun onFailure(e: Exception) {
                    Toast.makeText(itemView.context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        init {
            imageView.setOnPhotoTapListener { _, _, _ ->
                tapListener?.invoke()
            }
        }

        fun init(image: Page) {
            ImageLoader
                .request()
                .url(image.imageUrl)
                .progressListener(progressListener)
                .wrapHeight(true)
                .target(imageView)
        }
    }
}