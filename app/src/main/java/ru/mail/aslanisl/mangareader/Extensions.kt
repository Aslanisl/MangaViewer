package ru.mail.aslanisl.mangareader

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.MediatorLiveData
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import ru.mail.aslanisl.mangareader.data.base.UIData

fun <T> getLoadingLiveData(): MediatorLiveData<UIData<T>> {
    val liveData = MediatorLiveData<UIData<T>>()
    liveData.postValue(UIData.loading())
    return liveData
}

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int, @ColorInt colorInt: Int? = null): Drawable? {
    val drawable = try {
        var temp = ContextCompat.getDrawable(this, drawableRes)
        if (temp != null) return temp
        temp = VectorDrawableCompat.create(resources, drawableRes, theme)
        if (temp != null) return temp
        AppCompatResources.getDrawable(this, drawableRes)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    colorInt?.let { drawable?.setColorFilter(colorInt, PorterDuff.Mode.MULTIPLY) }
    return drawable
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
