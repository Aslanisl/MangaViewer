package ru.mail.aslanisl.mangareader.features.base.loadingAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import ru.mail.aslanisl.mangareader.R

private const val TYPE_LOADING = 100

abstract class LoadingRecyclerAdapter<T, VH : RecyclerView.ViewHolder>
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var enableLoading = true

    private var loadingHolderPosition = -1

    private var scrollListener: LoadingScrollListener = LoadingScrollListener()
    private var loadModeListener: (() -> Unit)? = null

    var visibleThreshold
        get() = scrollListener.visibleThreshold
        set(value) {
            scrollListener.visibleThreshold = value
        }

    val items = mutableListOf<T>()

    fun setLoadMore(listener: (T?) -> Unit) {
        loadModeListener = {
            if (enableLoading) {
                val lastItem = if (items.isEmpty()) null else items.getOrNull(items.size - 1)
                listener.invoke(lastItem)
            }
        }
        scrollListener.listener = loadModeListener
    }

    fun updateScrollListener() {
        scrollListener.update()
    }

    fun addItems(items: List<T>?) {
        items ?: return
        setShowLoading(items.size == visibleThreshold)
        if (this.items.containsAll(items)) return
        val startPosition = itemCount
        this.items.addAll(items)
        notifyItemRangeInserted(startPosition, items.size)
    }

    fun updateItems(items: List<T>?) {
        if (items == null){
            this.items.clear()
            notifyDataSetChanged()
            return
        }
        updateScrollListener()
        setShowLoading(items.size == visibleThreshold)
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = items[position]

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(scrollListener)
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(scrollListener)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): VH

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> {
                val rootView =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)

                LoadingViewHolder(rootView)
            }
            else -> onCreateItemViewHolder(parent, viewType)
        }
    }

    abstract fun onBindItemViewHolder(holder: VH, position: Int)

    @Suppress("UNCHECKED_CAST")
    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = holder.itemViewType
        when (itemType) {
            TYPE_LOADING -> { /** Do nothing */ }
            else -> (holder as? VH)?.let { onBindItemViewHolder(it, position) }
        }
    }

    fun setShowLoading(show: Boolean) {
        if (enableLoading == show) return
        enableLoading = show
        notifyDataSetChanged()
    }

    open fun getItemLoadingCount() = items.size

    override fun getItemCount(): Int {
        var childItemCount = getItemLoadingCount()
        if (enableLoading) {
            loadingHolderPosition = childItemCount
            childItemCount++
        }
        return childItemCount
    }

    open fun getItemLoadingViewType(position: Int): Int = 0

    override fun getItemViewType(position: Int): Int {
        return if (position == loadingHolderPosition && enableLoading) {
            TYPE_LOADING
        } else this.getItemLoadingViewType(position)
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}