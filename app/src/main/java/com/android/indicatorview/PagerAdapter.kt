package com.android.indicatorview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter

class CustomPagerAdapter(private val mContext: Context) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return 5
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.page, container, false) as LinearLayout
        if (position == 0) {
            layout.setBackgroundColor(mContext.resources.getColor(R.color.color0))
        } else if (position == 1) {
            layout.setBackgroundColor(mContext.resources.getColor(R.color.color1))
        } else if (position == 2) {
            layout.setBackgroundColor(mContext.resources.getColor(R.color.color2))
        } else if (position == 3) {
            layout.setBackgroundColor(mContext.resources.getColor(R.color.color3))
        } else if (position == 4) {
            layout.setBackgroundColor(mContext.resources.getColor(R.color.color4))
        }
        container.addView(layout)
        return layout
    }
}
