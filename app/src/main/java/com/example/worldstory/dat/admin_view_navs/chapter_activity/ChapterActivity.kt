package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.worldstory.dat.admin_adapter.ViewPagerAdapter
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ChapterActivity : AppCompatActivity() {
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chapter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val intent: Intent = getIntent()
        val storyID: Int = intent.getIntExtra("ID", -1)
        val type = intent.getIntExtra("type", 0)

        storyViewModel =
            StoryViewModelFactory(DatabaseHelper(this), type).create(StoryViewModel::class.java)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewpp = findViewById<ViewPager2>(R.id.vpp2)
        viewpp.invalidate()
        ///adapter
        val adapter = ViewPagerAdapter(this, storyID,type)
        viewpp.adapter = adapter
        TabLayoutMediator(tabLayout, viewpp) { tab, position ->
            when (position) {
                0 -> tab.text = "About story"
                1 -> tab.text = "About comments"
                2 -> tab.text = "About rates"
            }
        }.attach()

        findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
            .setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else finish()
        }

    }

    override fun onDestroy() {
        storyViewModel.resetValue()
        super.onDestroy()
    }
}