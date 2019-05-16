package ru.mail.aslanisl.mangareader.utils.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.URL
import kotlin.coroutines.CoroutineContext

object ImageLoadService : KoinComponent, CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val targetViews = mutableMapOf<String, ImageView>()
    private val queueUrlLoad = mutableSetOf<String>()

    private val cacheService: ImageCacheService by inject()

    fun loadImagesCache(urls: List<String>) {

    }

    private var currentRequest: ImageRequest.Builder? = null

    fun loadUrl(url: String): ImageLoadService {
        currentRequest = ImageRequest.Builder().url(url)
        return this
    }

    fun into(target: ImageView) {
        currentRequest?.into(target)
        makeRequest()
    }

    private fun makeRequest() {
        val request = currentRequest?.build() ?: return
        launch {
            val cache = loadFromCache(request.url)
            if (cache != null) {
                request.target?.post { request.target.setImageBitmap(cache) }
            }
            val bitmap = loadFromNet(request.url)
            if (bitmap != null) {
                request.target?.post { request.target.setImageBitmap(bitmap) }
            }
        }
    }

    @WorkerThread
    private fun loadFromCache(src: String): Bitmap? {
        val exist = cacheService.isBitmapExist(src)
        if (exist) {
            return cacheService.loadImage(src)
        }
        return null
    }

    @WorkerThread
    private fun loadFromNet(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            connection.connectTimeout = 60 * 1000
            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            cacheService.saveBitmap(src, bitmap)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}