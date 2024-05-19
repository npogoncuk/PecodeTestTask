package com.nazar.pecodetesttask

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nazar.pecodetesttask.databinding.ActivityMainBinding
import com.nazar.pecodetesttask.view_pager.ViewPagerAdapter

class MainActivity : AppCompatActivity(), IViewPagerHost {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private lateinit var viewPagerHost: IViewPagerHost
    private lateinit var viewPagerStateSaver: IViewPagerStateSaver

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewPagerHost.canSwipeLeft) swipeLeft()
            else finishAndRemoveTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.pager) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPagerHost = IViewPagerHost.Default(binding.pager)
        viewPagerStateSaver = IViewPagerStateSaver.SharedPrefImpl(this)

        binding.pager.adapter = ViewPagerAdapter(this)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        intent?.extras?.getInt(INotificationHandler.PENDING_INTENT_EXTRAS_PAGE_KEY, -1)?.takeIf { it != -1 }?.let { pageToOpen ->
            viewPagerStateSaver.saveCurrentPageNumber(pageToOpen)
            intent = null
        }
    }

    override fun swipeLeft() = viewPagerHost.swipeLeft()
    override fun swipeRight() = viewPagerHost.swipeRight()

    override val canSwipeLeft: Boolean by lazy {
        viewPagerHost.canSwipeLeft
    }

    override fun onStart() {
        super.onStart()
        binding.pager.setCurrentItem(viewPagerStateSaver.getCurrentPageNumber(), false)
    }

    override fun onStop() {
        super.onStop()
        // save state of viewPager in onStop because there is no guaranty that onDestroy() will be called
        viewPagerStateSaver.saveCurrentPageNumber(binding.pager.currentItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}