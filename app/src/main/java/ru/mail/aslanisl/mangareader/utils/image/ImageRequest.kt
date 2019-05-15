package ru.mail.aslanisl.mangareader.utils.image

import android.widget.ImageView

data class ImageRequest(val url: String, val target: ImageView?) {

    class Builder {
        private var target: ImageView? = null
        private var url: String? = null

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun into(target: ImageView?): Builder? {
            this.target = target
            return this
        }

        fun build(): ImageRequest? {
            val url = url ?: return null
            val target = target ?: return null
            return ImageRequest(url, target)
        }
    }
}