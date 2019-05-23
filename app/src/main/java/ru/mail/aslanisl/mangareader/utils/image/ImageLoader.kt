package ru.mail.aslanisl.mangareader.utils.image

import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

object ImageLoader : KoinComponent, CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val cacheService: ImageCacheService by inject()
    private val requests = mutableListOf<RequestService>()

    fun loadImagesCache(urls: List<String>) {

    }

    fun request() = RequestBuilder(this)

    internal fun load(requestBuilder: RequestBuilder) {
        val target = requestBuilder.targetView?.get() ?: return
        loadUrl(requestBuilder.urlImage, target, requestBuilder.listener)
    }

    private fun loadUrl(url: String?, target: ImageView, progressListener: NetProgressListener? = null) {
        synchronized(requests) {
            val sameTargetRequest = requests.firstOrNull { it.target?.hashCode() == target.hashCode() }
            if (sameTargetRequest != null) {
                sameTargetRequest.clearTarget()
                sameTargetRequest.clearProgressListener()
            }

            if (url.isNullOrEmpty()) {
                target.setImageBitmap(null)
                return@synchronized
            }

            val service = RequestService(target.context, url, target, progressListener, cacheService, this)
            service.makeRequest()
            requests.add(service)
        }
    }

    internal fun removeRequest(requestService: RequestService) {
        synchronized(requests) {
            requests.remove(requestService)
        }
    }
}