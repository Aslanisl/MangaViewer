package ru.mail.aslanisl.mangareader.features.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class FlowLayout : ViewGroup {

    private var line_height_space: Int = 0

    class LayoutParams(var horizontalSpacing: Int, var verticalSpacing: Int) : ViewGroup.LayoutParams(0, 0)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = View.MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        val count = childCount
        var line_height_space = 0

        var xpos = paddingLeft
        var ypos = paddingTop

        val childHeightMeasureSpec: Int
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
        } else {
            childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        }

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp = child.layoutParams as LayoutParams
                child.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST), childHeightMeasureSpec)
                val childw = child.measuredWidth
                line_height_space = Math.max(line_height_space, child.measuredHeight + lp.verticalSpacing)

                if (xpos + childw > width) {
                    xpos = paddingLeft
                    ypos += line_height_space
                }

                xpos += childw + lp.horizontalSpacing
            }
        }
        this.line_height_space = line_height_space

        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.UNSPECIFIED) {
            height = ypos + line_height_space

        } else if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            if (ypos + line_height_space < height) {
                height = ypos + line_height_space
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return generateDefaultLayoutParams()
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(16, 2)
    }

    @SuppressLint("ResourceType")
    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        val attributes = intArrayOf(android.R.attr.layout_marginBottom, android.R.attr.layout_marginEnd)
        val ta = context.obtainStyledAttributes(attrs, attributes)
        val marginBottom = ta.getDimensionPixelOffset(0, 2)
        val marginEnd = ta.getDimensionPixelOffset(1, 16)
        ta.recycle()
        return LayoutParams(marginEnd, marginBottom)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val width = r - l
        var xpos = paddingLeft
        var ypos = paddingTop

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val childw = child.measuredWidth
                val childh = child.measuredHeight
                val lp = child.layoutParams as LayoutParams
                if (xpos + childw > width) {
                    xpos = paddingLeft
                    ypos += line_height_space
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh)
                xpos += childw + lp.horizontalSpacing
            }
        }
    }
}