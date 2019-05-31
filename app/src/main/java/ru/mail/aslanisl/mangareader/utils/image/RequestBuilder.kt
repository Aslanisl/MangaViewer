package ru.mail.aslanisl.mangareader.utils.image

import android.widget.ImageView
import java.lang.ref.WeakReference

class RequestBuilder(private val loadService: ImageLoader) {

    internal var urlImage: String? = null
        private set
    internal var targetView: WeakReference<ImageView>? = null
        private set
    internal var listener: NetProgressListener? = null
        private set
    internal var wrapHeight: Boolean = false
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

    fun wrapHeight(wrap: Boolean): RequestBuilder {
        this.wrapHeight = wrap
        return this
    }

    fun load() = loadService.load(this)
}