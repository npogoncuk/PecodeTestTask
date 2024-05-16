package com.nazar.pecodetesttask

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nazar.pecodetesttask.databinding.ActivityMainBinding
import com.nazar.pecodetesttask.view_pager.ViewPagerAdapter

class MainActivity : AppCompatActivity(), ViewPagerHost {

    private lateinit var binding: ActivityMainBinding
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

        binding.pager.adapter = ViewPagerAdapter(this)
    }

    private var pagerCurrentItem: Int
        get() = binding.pager.currentItem
        set(value) {
            if (value < 0) return
            binding.pager.currentItem = value
        }

    override fun swipeRight() {
        pagerCurrentItem++
    }

    override fun swipeLeft() {
        pagerCurrentItem--
    }

}

interface ViewPagerHost {
    fun swipeRight()

    fun swipeLeft()
}