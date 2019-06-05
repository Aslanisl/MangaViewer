package ru.mail.aslanisl.mangareader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.MediatorLiveData
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import ru.mail.aslanisl.mangareader.data.base.UIData
import kotlin.math.roundToInt

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

fun EditText.onDoneClicked(hideKeyboard: Boolean = true, listener: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener.invoke()
            if (hideKeyboard) this.hideKeyboard()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE)
        as? InputMethodManager

    this.windowToken?.let {
        imm?.hideSoftInputFromWindow(it, 0)
    }
}

fun getString(@StringRes resId: Int): String = App.instance.getString(resId)

fun <T> catch(defaultValue: T, block: () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        defaultValue
    }
}

fun Context.getDimensionPixel(@DimenRes id: Int) = catch(0) { resources.getDimensionPixelSize(id) }

fun Bitmap.scaleDownToWidth(width: Int): Bitmap {
    if (this.width < width) return this

    val scaleFactor = this.width / width
    return Bitmap.createScaledBitmap(this, width, (this.height.toFloat() / scaleFactor).roundToInt(), false)
}

fun Bitmap.scaleDownToMax(size: Int): Bitmap {
    if (this.width <= size || this.height <= size) return this
    val scaleFactor = Math.min(this.width.toFloat() / size, this.height.toFloat() / size)
    return Bitmap.createScaledBitmap(this, (this.width / scaleFactor).toInt(), (this.height / scaleFactor).toInt(), false)
}