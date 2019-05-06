package ru.mail.aslanisl.mangareader.features.base.loadingAdapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class LoadingScrollListener : RecyclerView.OnScrollListener() {
    var visibleThreshold = 20
        set(value) {
            field = value
            update()
        }

    var listener: (() -> Unit)? = null
    private var previousTotalItemCount = 0
    private var loadingMode = true

    fun update() {
        previousTotalItemCount = 0
        loadingMode = true
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        // Now work only with linear layout manager
        val layoutManager = recyclerView.layoutManager ?: return

        val visibleItemCount = getVisibleItemCount(layoutManager) ?: return
        val totalItemCount = getItemCount(layoutManager) ?: return
        val lastVisiblePosition = getLastVisiblePosition(layoutManager) ?: return

        if (loadingMode && totalItemCount > previousTotalItemCount) {
            loadingMode = false
            previousTotalItemCount = totalItemCount
        }
        if (!loadingMode && visibleItemCount + lastVisiblePosition >= totalItemCount - visibleThreshold) {
            loadingMode = true
            listener?.invoke()
        }
    }

    private fun getVisibleItemCount(layoutManager: RecyclerView.LayoutManager): Int? {
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.childCount
            is StaggeredGridLayoutManager -> layoutManager.childCount
            else -> null
        }
    }

    private fun getItemCount(layoutManager: RecyclerView.LayoutManager): Int? {
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.itemCount
            is StaggeredGridLayoutManager -> layoutManager.itemCount
            else -> null
        }
    }

    private fun getLastVisiblePosition(layoutManager: RecyclerView.LayoutManager): Int? {
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val positions = layoutManager.findLastCompletelyVisibleItemPositions(null)
                getLastVisibleItem(positions)
            }
            else -> null
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        return lastVisibleItemPositions.max() ?: 0
    }
}