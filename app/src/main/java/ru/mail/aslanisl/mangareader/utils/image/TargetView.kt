package ru.mail.aslanisl.mangareader.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup.LayoutParams
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.WorkerThread
import ru.mail.aslanisl.mangareader.scaleDownToMax
import ru.mail.aslanisl.mangareader.utils.image.TargetView.SizeStatus.MEASURING
import ru.mail.aslanisl.mangareader.utils.image.TargetView.SizeStatus.READY
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min
import kotlin.math.roundToInt

private const val PENDING_SIZE = 0

class TargetView(val target: WeakReference<ImageView>, private val wrapHeight: Boolean) : OnAttachStateChangeListener {

    private enum class SizeStatus {
        MEASURING,
        READY
    }

    private var height: Int = 0
    private var width: Int = 0

    private var sizeStatus = MEASURING
    private var layoutListener: SizeLayoutListener? = null

    private var imageToSet: Bitmap? = null
    private var imageSet = AtomicBoolean(false)

    private var viewDetached = false

    companion object {
        fun init(target: ImageView, wrapHeight: Boolean): TargetView {
            return TargetView(WeakReference(target), wrapHeight)
        }

        var maxDisplayLength: Int? = null
    }

    init {
        target.get()?.addOnAttachStateChangeListener(this)

        attachListeners()
    }

    fun checkSizes() {
        val viewHeight = getViewHeight()
        val viewWidth = getViewWidth()
        if (isDimensionValid(viewHeight).not() || isDimensionValid(viewWidth).not()) return

        height = viewHeight
        width = viewWidth
        sizeStatus = READY

        if (imageSet.get().not()) return

        setImageCheckSizes(imageToSet)
        imageSet.set(false)
    }

    @WorkerThread
    fun setImageBitmap(image: Bitmap?) {
        if (sizeStatus == MEASURING) {
            imageSet.set(true)
            imageToSet = image
            return
        }
        setImageCheckSizes(image)
    }

    @WorkerThread
    private fun setImageCheckSizes(image: Bitmap?) {
        if (image == null) {
            target.get()?.post { target.get()?.setImageBitmap(null) }
            return
        }

        if (wrapHeight) {
            val widthFactor = image.width / width

            val imageWidthMoreWhenView = widthFactor > 1

            if (imageWidthMoreWhenView.not()) {
                val viewHeight = width / image.width * image.height

                target.get()?.post {
                    val view = target.get() ?: return@post
                    view.layoutParams.height = viewHeight
                    view.setImageBitmap(image)
                    view.requestLayout()
                }
                return
            }

            val scale = width.toFloat() / image.width
            val height = (image.height * scale).roundToInt()

            val scaled = Bitmap.createScaledBitmap(image, width, height, false)

            target.get()?.post {
                val view = target.get() ?: return@post
                view.layoutParams.height = height
                view.setImageBitmap(scaled)
                view.requestLayout()
            }
            return
        }

        val scaled = image.scaleDownToMax(min(width, height))
        target.get()?.post {
            val view = target.get() ?: return@post
            view.setImageBitmap(scaled)
        }
    }

    private fun attachListeners() {
        val observer = target.get()?.viewTreeObserver
        if (observer == null || observer.isAlive.not()) return

        if (layoutListener == null) {
            layoutListener = SizeLayoutListener(this)
            observer.addOnPreDrawListener(layoutListener)
        }
    }

    private fun clearListeners() {
        val observer = target.get()?.viewTreeObserver
        if (observer == null || observer.isAlive.not()) return

        if (layoutListener != null) {
            observer.removeOnPreDrawListener(layoutListener)
        }

        layoutListener = null
    }

    private fun getViewHeight(): Int {
        val view = target.get() ?: return PENDING_SIZE
        val verticalPadding = view.paddingTop + view.paddingBottom
        val layoutParams = view.layoutParams
        val layoutParamSize = layoutParams?.height ?: PENDING_SIZE
        return getTargetDimen(view.height, layoutParamSize, verticalPadding)
    }

    private fun getViewWidth(): Int {
        val view = target.get() ?: return PENDING_SIZE

        val horizontalPadding = view.paddingLeft + view.paddingRight
        val layoutParams = view.layoutParams
        val layoutParamSize = layoutParams?.width ?: PENDING_SIZE
        return getTargetDimen(view.width, layoutParamSize, horizontalPadding)
    }

    private fun getTargetDimen(viewSize: Int, paramSize: Int, paddingSize: Int): Int {
        val adjustedParamSize = paramSize - paddingSize
        if (adjustedParamSize > 0) {
            return adjustedParamSize
        }

        val adjustedViewSize = viewSize - paddingSize
        if (adjustedViewSize > 0) {
            return adjustedViewSize
        }

        val view = target.get() ?: return PENDING_SIZE

        if (!view.isLayoutRequested && paramSize == LayoutParams.WRAP_CONTENT) {
            return getMaxDisplayLength(view.context)
        }

        return PENDING_SIZE
    }

    private fun isDimensionValid(size: Int) = size > 0

    private fun getMaxDisplayLength(context: Context): Int {
        if (maxDisplayLength == null) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val displayDimensions = Point()
            windowManager?.defaultDisplay?.getSize(displayDimensions)
            maxDisplayLength = Math.max(displayDimensions.x, displayDimensions.y)
        }
        return maxDisplayLength!!
    }

    override fun onViewDetachedFromWindow(v: View?) {
        clearListeners()
        viewDetached = true
    }

    override fun onViewAttachedToWindow(v: View?) {
        attachListeners()
        viewDetached = false
    }

    private class SizeLayoutListener constructor(targetView: TargetView) : ViewTreeObserver.OnPreDrawListener {
        private val target: WeakReference<TargetView> = WeakReference(targetView)

        override fun onPreDraw(): Boolean {
            target.get()?.checkSizes()
            return true
        }
    }
}