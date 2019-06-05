package ru.mail.aslanisl.mangareader.utils.image

import android.graphics.Bitmap
import android.util.LruCache
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

    internal fun load(request: RequestBuilder) {
        val target = request.targetView?.get() ?: return

        synchronized(requests) {
            val sameTargetRequest = requests.firstOrNull { it.targetView.target.get()?.hashCode() == target.hashCode() }

            if (sameTargetRequest?.url == request.urlImage) {
                return@synchronized
            }
            if (sameTargetRequest != null) {
                sameTargetRequest.cancelRequest()
            }

//            cacheService.openDiskCache()

            val targetView = TargetView.init(target, request.wrapHeight)
            val service = RequestService(target.context, request.urlImage, request.listener, targetView, cacheService, this, memoryCache)
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