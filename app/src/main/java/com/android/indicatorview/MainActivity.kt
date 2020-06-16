package com.android.indicatorview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager = findViewById(R.id.viewPager) as ViewPager
        val indicatorView = findViewById(R.id.indicator) as IndicatorView
        val adapter = CustomPagerAdapter(this)
        viewPager.setAdapter(adapter)
        try {
            indicatorView.setViewPager(viewPager)
        } catch (e: PagesLessException) {
            e.printStackTrace()
        }
    }
}
