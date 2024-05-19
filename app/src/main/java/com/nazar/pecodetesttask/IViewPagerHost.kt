package com.nazar.pecodetesttask

import androidx.viewpager2.widget.ViewPager2

interface IViewPagerHost {

    val canSwipeLeft: Boolean

    fun swipeRight()

    fun swipeLeft()

    class Default(private val pager: ViewPager2) : IViewPagerHost {
        private var pagerCurrentItem: Int
            get() = pager.currentItem
            set(value) {
                if (value < 0) return
                pager.currentItem = value
            }

        override val canSwipeLeft: Boolean
            get() = pagerCurrentItem > 0
        override fun swipeRight() {
            pagerCurrentItem++
        }

        override fun swipeLeft() {
            pagerCurrentItem--
        }
    }
}