package ru.mail.aslanisl.mangareader.utils.image

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception
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

    fun loadUrl(url: String): ImageRequest.Builder {
        currentRequest = ImageRequest.Builder().url(url)
        return currentRequest!!
    }

    fun into(target: ImageView) {
        currentRequest?.into(target)
    }

    private fun makeRequest() {
        val request = currentRequest?.build() ?: return
        request.target ?: return
        launch {
            
        }
    }

    @WorkerThread
    private suspend fun checkAndLoad(url: String) {
        val exist = cacheService.isBitmapExist(url)

    }

    private suspend fun loadFromCache(src: String): Bitmap? {
        val bitmap = cacheService.loadImage(src)

    }

    private suspend fun loadFromNet(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
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