package ru.mail.aslanisl.mangareader.features.view

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import ru.mail.aslanisl.mangareader.dataModel.Page

class PageViewPagerAdapter : PagerAdapter() {

    private val pages = mutableListOf<Page>()

    fun updatePages(pages: List<Page>) {
        this.pages.clear()
        this.pages.addAll(pages)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view is PhotoView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(container.context)
        photoView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        Glide.with(container).load(pages[position].imageUrl).into(photoView)
        container.addView(photoView)
        return photoView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as PhotoView)
    }

    override fun getCount() = pages.size
}