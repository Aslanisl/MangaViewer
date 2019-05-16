package ru.mail.aslanisl.mangareader.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.WorkerThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.coroutines.CoroutineContext

private const val PROGRESS_STEP = 5f

class RequestService(
    context: Context,
    private val url: String,
    var target: ImageView?,
    private var progressListener: NetProgressListener?,
    private val cacheService: ImageCacheService,
    private val parentService: ImageLoadService
) : CoroutineScope, LifecycleObserver {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    init {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }
    }

    fun makeRequest() {
        launch {
            val cache = loadFromCache(url)
            if (cache != null) {
                target?.post { target?.setImageBitmap(cache) }
            }
            val bitmap = loadFromNet(url, cache == null)
            if (bitmap != null) {
                target?.post { target?.setImageBitmap(bitmap) }
            }
            finishWork()
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
    private fun loadFromNet(src: String, showLoading: Boolean): Bitmap? {
        return try {
            if (showLoading) {
                launch(Dispatchers.Main) {
                    progressListener?.onStart()
                }
            }

            val url = URL(src)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            connection.connectTimeout = 60 * 1000
            val inputStream = connection.getInputStream()
            val size = connection.contentLength
            val array = ByteArray(size)
            var byte: Byte
            var bytePosition = 0

            var currentProgress = 0f
            var showedProgress = -PROGRESS_STEP

            while (bytePosition < size) {
                byte = inputStream.read().toByte()
                array[bytePosition] = byte
                bytePosition++

                currentProgress = (bytePosition.toFloat() / array.size) * 100
                if (showedProgress + PROGRESS_STEP <= currentProgress) {
                    // Send to main thread
                    if (showLoading) {
                        launch(Dispatchers.Main) {
                            progressListener?.progressChanged(currentProgress)
                        }
                    }
                    showedProgress = currentProgress
                }
            }

            if (showLoading) {
                launch(Dispatchers.Main) {
                    progressListener?.onFinish()
                }
            }
            val bitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
            cacheService.saveBitmap(src, bitmap)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun finishWork() {
        cancelRequest()
        parentService.removeRequest(this)
    }

    fun clearTarget() {
        synchronized(this) {
            target = null
        }
    }

    fun clearProgressListener() {
        synchronized(this) {
            progressListener?.onCancel()
            progressListener = null
        }
    }

    fun cancelRequest() {
        job.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAnyLifecycleEvent(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            cancelRequest()
        }
    }
}