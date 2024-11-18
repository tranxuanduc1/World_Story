package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.worldstory.dat.admin_adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ChapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chapter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewpp = findViewById<ViewPager2>(R.id.vpp2)
        ///adapter
        val adapter = ViewPagerAdapter(this)
        viewpp.adapter = adapter
        TabLayoutMediator(tabLayout, viewpp) { tab, position ->
            when(position){
                0->tab.text="About story"
                1->tab.text="About comments"
                2->tab.text="About rates"
            }
        }.attach()
    }
}