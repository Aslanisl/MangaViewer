package ru.mail.aslanisl.mangareader.utils.image

import android.graphics.Bitmap
import android.util.LruCache
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.ref.SoftReference
import kotlin.coroutines.CoroutineContext

object ImageLoader : KoinComponent, CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val cacheService: ImageCacheService by inject()
    private val requests = mutableListOf<RequestService>()

    private val memoryCache: LruCache<String, SoftReference<Bitmap>>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, SoftReference<Bitmap>>(cacheSize) {
            override fun sizeOf(key: String, value: SoftReference<Bitmap>): Int {
                return (value.get()?.byteCount ?: 0) / 1024
            }
        }
    }

    fun loadImagesCache(urls: List<String>) {
        // TODO add implementation
    }

    fun request() = RequestBuilder(this)

    internal fun load(requestBuilder: RequestBuilder) {
        val target = requestBuilder.targetView?.get() ?: return
        loadUrl(requestBuilder.urlImage, target, requestBuilder.listener)
    }

    private fun loadUrl(url: String?, target: ImageView, progressListener: NetProgressListener? = null) {
        synchronized(requests) {
            val sameTargetRequest = requests.firstOrNull { it.target?.hashCode() == target.hashCode() }

            if (sameTargetRequest?.url == url) {
                return@synchronized
            }
            if (sameTargetRequest != null) {
                sameTargetRequest.cancelRequest()
            }

//            cacheService.openDiskCache()

            val service = RequestService(target.context, url, target, progressListener, cacheService, this, memoryCache)
            service.makeRequest()
            requests.add(service)
        }
    }

    internal fun removeRequest(requestService: RequestService) {
        synchronized(requests) {
            requests.remove(requestService)
//            if (requests.isEmpty()) cacheService.closeDiskCache()
        }
    }
}