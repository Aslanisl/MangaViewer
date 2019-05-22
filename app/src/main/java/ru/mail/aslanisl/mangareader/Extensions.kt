package ru.mail.aslanisl.mangareader

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
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
    var drawable: Drawable? = null
    run {
        try {
            drawable = ContextCompat.getDrawable(this, drawableRes)
            if (drawable != null) return@run
            drawable = VectorDrawableCompat.create(resources, drawableRes, theme)
            if (drawable != null) return@run
            drawable = AppCompatResources.getDrawable(this, drawableRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

fun Context.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message?.let { Toast.makeText(this, it, duration).show() }
}