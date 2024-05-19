package com.nazar.pecodetesttask

import android.content.Context

interface IViewPagerStateSaver {

    fun saveCurrentPageNumber(number: Int)

    fun getCurrentPageNumber(): Int

    class SharedPrefImpl(context: Context) : IViewPagerStateSaver {

        private val sharedPref = context.getSharedPreferences(VIEW_PAGER_STATE_SHARED_PREF, Context.MODE_PRIVATE)

        override fun saveCurrentPageNumber(number: Int) {
            sharedPref.edit()
                .putInt(CURRENT_PAGE_NUMBER_KEY, number)
                .apply()
        }

        override fun getCurrentPageNumber(): Int {
            return sharedPref.getInt(CURRENT_PAGE_NUMBER_KEY, 0)
        }

        companion object {
            const val VIEW_PAGER_STATE_SHARED_PREF = "view_pager_state"
            const val CURRENT_PAGE_NUMBER_KEY = "current_page_number"
        }

    }
}