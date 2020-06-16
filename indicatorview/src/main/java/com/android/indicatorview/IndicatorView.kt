package com.android.indicatorview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class IndicatorView : View, IndicatorInterface, ViewPager.OnPageChangeListener {
    private var viewPager: ViewPager? = null
    private lateinit var dots: Array<Dot?>
    private var animateDuration = DEFAULT_ANIMATE_DURATION
    private var radiusSelected = DEFAULT_RADIUS_SELECTED
    private var radiusUnselected = DEFAULT_RADIUS_UNSELECTED
    private var distance = DEFAULT_DISTANCE
    private var colorSelected = 0
    private var colorUnselected = 0
    private var currentPosition = 0
    private var beforePosition = 0
    private lateinit var animatorZoomIn: ValueAnimator
    private lateinit var animatorZoomOut: ValueAnimator

    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.IndicatorView)
        radiusSelected = typedArray.getDimensionPixelSize(
            R.styleable.IndicatorView_iv_radius_selected,
            DEFAULT_RADIUS_SELECTED
        )
        radiusUnselected = typedArray.getDimensionPixelSize(
            R.styleable.IndicatorView_iv_radius_unselected,
            DEFAULT_RADIUS_UNSELECTED
        )
        distance = typedArray.getInt(
            R.styleable.IndicatorView_iv_distance,
            DEFAULT_DISTANCE
        )
        colorSelected = typedArray.getColor(
            R.styleable.IndicatorView_iv_color_selected,
            Color.parseColor("#ffffff")
        )
        colorUnselected = typedArray.getColor(
            R.styleable.IndicatorView_iv_color_unselected,
            Color.parseColor("#ffffff")
        )
        typedArray.recycle()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        val yCenter = height / 2.toFloat()
        val d = distance + 2 * radiusUnselected
        val firstXCenter = width / 2 - ((dots.size - 1) * d / 2).toFloat()
        for (i in dots.indices) {
            dots[i]?.setCenter(if (i == 0) firstXCenter else firstXCenter + d * i, yCenter)
            dots[i]?.currentRadius = (if (i == currentPosition) radiusSelected else radiusUnselected)
            dots[i]?.setColor(if (i == currentPosition) colorSelected else colorUnselected)
            dots[i]?.setAlpha(if (i == currentPosition) 255 else radiusUnselected * 255 / radiusSelected)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = 2 * radiusSelected
        val width: Int
        val height: Int
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        width = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthSize
        } else {
            0
        }
        height = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            Math.min(desiredHeight, heightSize)
        } else {
            desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        for (dot in dots) {
            dot!!.draw(canvas)
        }
    }

    @Throws(PagesLessException::class)
    override fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        currentPosition = viewPager.currentItem
        viewPager.addOnPageChangeListener(this)
        viewPager.adapter?.getCount()?.let { initDot(it) }
        onPageSelected(currentPosition)
    }

    @Throws(PagesLessException::class)
    private fun initDot(count: Int) {
        if (count < 2) throw PagesLessException()
        dots = arrayOfNulls(count)
        for (i in dots.indices) {
            dots[i] = Dot()
        }
    }

    override fun setAnimateDuration(duration: Long) {
        animateDuration = duration
    }

    override fun setRadiusSelected(radius: Int) {
        radiusSelected = radius
    }

    override fun setRadiusUnselected(radius: Int) {
        radiusUnselected = radius
    }

    override fun setDistanceDot(distance: Int) {
        this.distance = distance
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) {
        beforePosition = currentPosition
        currentPosition = position
        if (beforePosition == currentPosition) {
            beforePosition = currentPosition + 1
        }
        dots[currentPosition]!!.setColor(colorSelected)
        dots[beforePosition]!!.setColor(colorUnselected)
        val animatorSet = AnimatorSet()
        animatorSet.duration = animateDuration
        animatorZoomIn = ValueAnimator.ofInt(radiusUnselected, radiusSelected)
        animatorZoomIn.addUpdateListener(object : AnimatorUpdateListener {
            var positionPerform = currentPosition
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val newRadius = valueAnimator.animatedValue as Int
                changeNewRadius(positionPerform, newRadius)
            }
        })
        animatorZoomOut = ValueAnimator.ofInt(radiusSelected, radiusUnselected)
        animatorZoomOut.addUpdateListener(object : AnimatorUpdateListener {
            var positionPerform = beforePosition
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val newRadius = valueAnimator.animatedValue as Int
                changeNewRadius(positionPerform, newRadius)
            }
        })
        animatorSet.play(animatorZoomIn).with(animatorZoomOut)
        animatorSet.start()
    }

    private fun changeNewRadius(positionPerform: Int, newRadius: Int) {
        if (dots[positionPerform]?.currentRadius !== newRadius) {
            dots[positionPerform]?.currentRadius = newRadius
            dots[positionPerform]?.setAlpha(newRadius * 255 / radiusSelected)
            invalidate()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    companion object {
        const val TAG = "IndicatorView"
        private const val DEFAULT_ANIMATE_DURATION: Long = 200
        private const val DEFAULT_RADIUS_SELECTED = 20
        private const val DEFAULT_RADIUS_UNSELECTED = 15
        private const val DEFAULT_DISTANCE = 40
    }
}
