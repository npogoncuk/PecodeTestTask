package com.nazar.pecodetesttask

import android.annotation.SuppressLint
import android.content.Context

interface IViewPagerStateSaver {

    fun saveCurrentPageNumber(number: Int)

    fun getCurrentPageNumber(): Int

    class SharedPrefImpl(context: Context) : IViewPagerStateSaver {

        private val sharedPref = context.getSharedPreferences(VIEW_PAGER_STATE_SHARED_PREF, Context.MODE_PRIVATE)

        @SuppressLint("ApplySharedPref")
        override fun saveCurrentPageNumber(number: Int) {
            sharedPref.edit()
                .putInt(CURRENT_PAGE_NUMBER_KEY, number)
                .commit()
        }

        override fun getCurrentPageNumber(): Int {
            return sharedPref.getInt(CURRENT_PAGE_NUMBER_KEY, 0)
        }
        companion object {
            private const val VIEW_PAGER_STATE_SHARED_PREF = "view_pager_state"
            private const val CURRENT_PAGE_NUMBER_KEY = "current_page_number"
        }

    }
}