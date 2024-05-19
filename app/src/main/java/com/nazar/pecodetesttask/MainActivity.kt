package com.nazar.pecodetesttask

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nazar.pecodetesttask.databinding.ActivityMainBinding
import com.nazar.pecodetesttask.view_pager.ViewPagerAdapter

class MainActivity : AppCompatActivity(), IViewPagerHost {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerHost: IViewPagerHost
    private lateinit var viewPagerStateSaver: IViewPagerStateSaver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.pager) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPagerHost = IViewPagerHost.Default(binding.pager)
        viewPagerStateSaver = IViewPagerStateSaver.SharedPrefImpl(this)

        binding.pager.adapter = ViewPagerAdapter(this)
    }

    override fun swipeLeft() = viewPagerHost.swipeLeft()
    override fun swipeRight() = viewPagerHost.swipeRight()

    override fun onStart() {
        super.onStart()
        binding.pager.setCurrentItem(viewPagerStateSaver.getCurrentPageNumber(), false)
    }

    override fun onStop() {
        super.onStop()
        // save state of viewPager in onStop because there is no guaranty that onDestroy() will be called
        viewPagerStateSaver.saveCurrentPageNumber(binding.pager.currentItem)
    }

}