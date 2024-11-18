package com.example.worldstory.duc.ducadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.worldstory.duc.ducfragment.Duc_LoveBookStories_Fragment
import com.example.worldstory.duc.ducfragment.Duc_ReadBookStories_Fragment

class Duc_FragmentPage_BookStories_Adapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager,lifeCycle) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                Duc_ReadBookStories_Fragment()
            }else -> {
                Duc_LoveBookStories_Fragment()
            }
        }
    }

    override fun getItemCount(): Int {
       return 2
    }


}