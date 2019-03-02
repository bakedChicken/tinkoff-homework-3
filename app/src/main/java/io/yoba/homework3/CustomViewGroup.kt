package io.yoba.homework3

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.res.use
import androidx.core.view.children
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

class CustomViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var spacing by Delegates.notNull<Int>()
    private var childHeight by Delegates.notNull<Int>()
    private var gravity by Delegates.notNull<Int>()

    init {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomViewGroup, 0, 0)

        attributes.use {
            spacing = it.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_spacing, 0)
            childHeight = it.getDimensionPixelSize(R.styleable.CustomViewGroup_cvg_child_height, 0)
            gravity = it.getInteger(R.styleable.CustomViewGroup_android_gravity, Gravity.LEFT)
        }
    }

    @SuppressLint("RtlHardcoded")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var currentWidth = 0

        var desiredWidth = 0
        var desiredHeight = 0

        children.forEach {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)

            if (currentWidth + it.measuredWidth > widthSize) {
                desiredHeight += childHeight + spacing
                desiredWidth = max(desiredWidth, currentWidth - spacing)
                currentWidth = 0
            }

            it.top = desiredHeight

            if (gravity == Gravity.RIGHT) {
                it.right = widthSize - currentWidth
            } else {
                it.left = currentWidth
            }

            currentWidth += it.measuredWidth + spacing
        }

        desiredHeight += childHeight + spacing

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> throw Throwable("")
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> throw Throwable("")
        }

        setMeasuredDimension(width, height)
    }

    @SuppressLint("RtlHardcoded")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEach {
            if (gravity == Gravity.RIGHT) {
                it.layout(it.right - it.measuredWidth, it.top, it.right, it.top + it.measuredHeight)
            } else {
                it.layout(it.left, it.top, it.left + it.measuredWidth, it.top + it.measuredHeight)
            }
        }
    }
}