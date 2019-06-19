package ru.mail.aslanisl.mangareader.features.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.piasy.biv.indicator.ProgressIndicator
import com.github.piasy.biv.view.BigImageView
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.features.view.PageAdapter.ImageViewHolder
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.show
import ru.mail.aslanisl.mangareader.utils.image.NetProgressListener

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
        private val imageView = itemView.findViewById<BigImageView>(R.id.image)
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

        private val listener by lazy {
            object : ProgressIndicator {
                override fun onFinish() {
                    imageView.show()
                    progressView.gone()
                }

                override fun getView(parent: BigImageView?): View {
                    return progressView
                }

                override fun onProgress(progress: Int) {
                    progressView.progress = progress
                }

                override fun onStart() {
                    imageView.gone()
                    progressView.show()
                }
            }
        }

        init {
            imageView.setOnClickListener { tapListener?.invoke() }
            imageView.setProgressIndicator(listener)
        }

        fun init(image: Page) {
            imageView.showImage(Uri.parse(image.imageUrl))
//            ImageLoader
//                .request()
//                .url(image.imageUrl)
//                .progressListener(progressListener)
//                .wrapHeight(true)
//                .target(imageView)
        }
    }
}