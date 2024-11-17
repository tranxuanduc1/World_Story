package com.example.worldstory.ducfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.FragmentDucBookStoriesUserBinding
import com.example.worldstory.ducadapter.Duc_FragmentPage_BookStories_Adapter
import com.example.worldstory.ducviewmodel.DucGenreViewModel
import com.example.worldstory.ducviewmodel.DucStoryViewModel
import com.example.worldstory.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.ducviewmodelfactory.DucStoryViewModelFactory
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Duc_BookStories_User_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Duc_BookStories_User_Fragment : Fragment() {

    private lateinit var binding: FragmentDucBookStoriesUserBinding
    private lateinit var pageAdapter: Duc_FragmentPage_BookStories_Adapter
    private val ducStoryViewModel: DucStoryViewModel by viewModels{
        DucStoryViewModelFactory(requireContext())
    }
    private val ducGenreViewModel: DucGenreViewModel by viewModels{
        DucGenreViewModelFactory(requireContext())
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDucBookStoriesUserBinding.inflate(layoutInflater)
        val view=binding.root
        //---------------------------------------
        pageAdapter= Duc_FragmentPage_BookStories_Adapter(childFragmentManager, lifecycle)
        binding.viewPaper2BookStoriesFragment.adapter=pageAdapter
        binding.tabLayoutBookStoriesFragment.addOnTabSelectedListener(
            object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.viewPaper2BookStoriesFragment.currentItem=tab.position
                    }

                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }


            })
        binding.viewPaper2BookStoriesFragment.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tabLayoutBookStoriesFragment.selectTab(
                        binding.tabLayoutBookStoriesFragment.getTabAt(position)
                    )
                }
            }
        )
        // Inflate the layout for this fragment
        return view


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookStoriesUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Duc_BookStories_User_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}