package com.example.worldstory.ducadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.worldstory.ducfragment.Duc_BookStories_User_Fragment
import com.example.worldstory.ducfragment.Duc_ComicStories_User_Fragment
import com.example.worldstory.ducfragment.Duc_Setting_User_Fragment
import com.example.worldstory.ducfragment.Duc_TextStories_User_Fragment

class Duc_FragmentPage_UserHome_Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager,lifecycle)
{
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                Duc_ComicStories_User_Fragment()
            }
            1 ->{
                Duc_TextStories_User_Fragment()
            }
            2 ->{
                Duc_BookStories_User_Fragment()
            }
            else ->{
                Duc_Setting_User_Fragment()
            }

        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}