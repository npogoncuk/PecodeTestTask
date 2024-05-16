package com.nazar.pecodetesttask.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    //private var numberOfPages = 1
    override fun getItemCount(): Int = Int.MAX_VALUE//numberOfPages

    override fun createFragment(position: Int): Fragment {
        return NotificationFragment.newInstance(position + 1)
    }
}