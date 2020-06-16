package com.android.indicatorview

import androidx.viewpager.widget.ViewPager


interface IndicatorInterface {
    @Throws(PagesLessException::class)
    fun setViewPager(viewPager: ViewPager)

    fun setAnimateDuration(duration: Long)

    /**
     *
     * @param radius: radius in pixel
     */
    fun setRadiusSelected(radius: Int)

    /**
     *
     * @param radius: radius in pixel
     */
    fun setRadiusUnselected(radius: Int)

    /**
     *
     * @param distance: distance in pixel
     */
    fun setDistanceDot(distance: Int)
}
