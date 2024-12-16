package com.example.worldstory.view.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucUserHomeBinding
import com.example.worldstory.duc.ducadapter.Duc_FragmentPage_UserHome_Adapter
import com.google.android.material.tabs.TabLayout

class DucUserHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucUserHomeBinding
    private lateinit var pageAdapter: Duc_FragmentPage_UserHome_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDucUserHomeBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        // turn off darkmode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(view)
        pageAdapter = Duc_FragmentPage_UserHome_Adapter(supportFragmentManager, lifecycle)
        // tao tab con moi
        //tab1
        var tab1: TabLayout.Tab=binding.tabLayoutUserHome.newTab()
        tab1.setText(R.string.titleTab1)
        tab1.setIcon(R.drawable.icon_comic)
        //tab2
        var tab2: TabLayout.Tab=binding.tabLayoutUserHome.newTab()
        tab2.setText(R.string.titleTab2)
        tab2.setIcon( R.drawable.icon_text)
        //tab3
        var tab3: TabLayout.Tab=binding.tabLayoutUserHome.newTab()
        tab3.setText(R.string.titleTab3)
        tab3.setIcon( R.drawable.icon_book)
        //tab4
        var tab4: TabLayout.Tab=binding.tabLayoutUserHome.newTab()
        tab4.setText(R.string.titleTab4)
        tab4.setIcon( R.drawable.icon_setting)
        //add tab
        binding.tabLayoutUserHome.addTab(tab1)
        binding.tabLayoutUserHome.addTab(tab2)
        binding.tabLayoutUserHome.addTab(tab3)
        binding.tabLayoutUserHome.addTab(tab4)

        ///
        binding.viewPaper2UserHome.isUserInputEnabled=false
        binding.viewPaper2UserHome.adapter=pageAdapter
        binding.tabLayoutUserHome.addOnTabSelectedListener(
            object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPaper2UserHome.currentItem=tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        binding.viewPaper2UserHome.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tabLayoutUserHome.selectTab(
                        binding.tabLayoutUserHome.getTabAt(position))
                }
            })
    }
}