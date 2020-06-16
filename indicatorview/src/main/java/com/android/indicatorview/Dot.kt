package com.android.indicatorview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Dot {
    private val paint: Paint
    private val center: PointF

    init {
        paint = Paint()
        paint.isAntiAlias = true
        center = PointF()
    }

    var currentRadius = 0

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    fun setCenter(x: Float, y: Float) {
        center[x] = y
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, currentRadius.toFloat(), paint)
    }
}
