package ru.mail.aslanisl.mangareader.utils.image

import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

object ImageLoadService : KoinComponent, CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val cacheService: ImageCacheService by inject()
    private val requests = mutableListOf<RequestService>()

    fun loadImagesCache(urls: List<String>) {

    }

    fun loadUrl(url: String, target: ImageView, progressListener: NetProgressListener? = null) {
        synchronized(requests) {
            val sameTargetRequest = requests.firstOrNull { it.target?.hashCode() == target.hashCode() }
            if (sameTargetRequest != null) {
                sameTargetRequest.clearTarget()
                sameTargetRequest.clearProgressListener()
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