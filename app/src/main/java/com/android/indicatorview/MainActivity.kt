package com.android.indicatorview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = CustomPagerAdapter(this)
        viewPager.setAdapter(adapter)
        try {
            indicatorView.setViewPager(viewPager)
        } catch (e: PagesLessException) {
            e.printStackTrace()
        }
    }
}
