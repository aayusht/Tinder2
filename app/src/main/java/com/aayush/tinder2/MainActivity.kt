package com.aayush.tinder2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ViewPager>(R.id.viewPager).adapter = CardPagerAdapter()
    }

    inner class CardPagerAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return 6
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val layout = LayoutInflater.from(this@MainActivity).inflate(R.layout.person_card, container, false)
            container?.addView(layout)
            return layout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

    }
}
