package ru.mail.aslanisl.mangareader.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

private const val PROGRESS_STEP = 1f
private const val CONNECTION_TIMEOUT = 60 * 1000

class RequestService(
    context: Context,
    val url: String?,
    private var progressListener: NetProgressListener?,
    val targetView: TargetView,
    private val cacheService: ImageCacheService,
    private val parentService: ImageLoader,
    private val memoryCache: LruCache<String, SoftReference<Bitmap>>
) : CoroutineScope, LifecycleObserver {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private val mainHandler = Handler(Looper.getMainLooper())

    private var cancelNetJob = AtomicBoolean(false)
    private var cancelSetImage = AtomicBoolean(false)

    private var cancelProgressListener = AtomicBoolean(false)

    init {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }
    }

    fun makeRequest(forceLoadNet: Boolean = false) {
        if (forceLoadNet.not() && loadFromMemory()) return

        targetView.setImageBitmap(null)

        launch {
            val url = url
            if (url.isNullOrEmpty()) {
                return@launch
            }

            val loadIsCacheExist = cacheService.isNeedToLoad(url) || forceLoadNet

            val cache = loadFromCache(url)
            if (cache != null) {
                if (cancelSetImage.get().not()) {
                    targetView.setImageBitmap(cache)
                }

                if (loadIsCacheExist.not()) {
                    putToMemory(url, cache)
                    postUI {
                        if (cancelProgressListener.get().not()) progressListener?.onFinish()
                        finishWork()
                    }
                    return@launch
                }
            }

            val bitmap = loadFromNet(url, cache == null)
            if (bitmap != null) {
                if (cancelSetImage.get().not()) {
                    targetView.setImageBitmap(bitmap)
                }
            }
            postUI { finishWork() }
        }
    }

    private fun loadFromMemory(): Boolean {
        val memoryBitmap = memoryCache[url]?.get() ?: return false
        // Seems like we don't need to load
        if (cancelSetImage.get()) return true

        launch {
            targetView.setImageBitmap(memoryBitmap)
            postUI {
                if (cancelProgressListener.get().not()) progressListener?.onFinish()
                finishWork()
            }
        }

        return true
    }

    private fun putToMemory(key: String, bitmap: Bitmap) {
        memoryCache.put(key, SoftReference(bitmap))
    }

    @WorkerThread
    private fun loadFromCache(src: String): Bitmap? {
        return cacheService.loadImage(src)
    }

    @WorkerThread
    private fun loadFromNet(src: String, showLoading: Boolean): Bitmap? {
        return try {
            if (showLoading) {
                if (cancelProgressListener.get().not()) {
                    postUI {
                        if (cancelProgressListener.get().not()) progressListener?.onStart()
                    }
                }
            }

            val url = URL(src)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            connection.connectTimeout = CONNECTION_TIMEOUT
            connection.readTimeout = CONNECTION_TIMEOUT
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
                    if (showLoading) {
                        if (cancelProgressListener.get().not()) {
                            postUI {
                                if (cancelProgressListener.get().not()) progressListener?.progressChanged(currentProgress)
                            }
                        }
                    }
                    showedProgress = currentProgress

                    if (cancelNetJob.get()) {
                        return null
                    }
                }
            }

            val bitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
            cacheService.saveBitmap(src, bitmap)
            putToMemory(src, bitmap)

            if (showLoading) {
                if (cancelProgressListener.get().not()) {
                    postUI {
                        if (cancelProgressListener.get().not()) progressListener?.onFinish()
                    }
                }
            }
            bitmap
        } catch (e: Exception) {
            if (cancelProgressListener.get().not()) {
                postUI {
                    if (cancelProgressListener.get().not()) progressListener?.onFailure(e)
                }
            }
            e.printStackTrace()
            null
        }
    }

    private fun finishWork() {
        cancelRequest(false)
    }

    fun clearProgressListener() {
        postUI {
            progressListener?.onCancel()
        }
        cancelProgressListener.set(true)
    }

    fun cancelRequest(sendCancel: Boolean = true) {
        if (sendCancel) {
            postUI {
                if (cancelProgressListener.get().not()) progressListener?.onCancel()
                cancelProgressListener.set(true)
            }
        }
        cancelSetImage.set(true)
        cancelNetJob.set(true)
        job.cancel()
        parentService.removeRequest(this)
    }

    private fun postUI(action: () -> Unit) {
        mainHandler.post(action)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAnyLifecycleEvent(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            cancelRequest()
        }
    }
}