package ru.mail.aslanisl.mangareader.utils.image

import android.widget.ImageView
import java.lang.ref.WeakReference

class RequestBuilder(private val loadService: ImageLoader) {

    var urlImage: String? = null
        private set
    var targetView: WeakReference<ImageView>? = null
        private set
    var listener: NetProgressListener? = null
        private set

    fun url(url: String?): RequestBuilder {
        urlImage = url
        return this
    }

    fun target(imageView: ImageView, load: Boolean = true): RequestBuilder {
        targetView = WeakReference(imageView)
        if (load) loadService.load(this)
        return this
    }

    fun progressListener(listener: NetProgressListener?): RequestBuilder {
        this.listener = listener
        return this
    }

    fun load() = loadService.load(this)
}