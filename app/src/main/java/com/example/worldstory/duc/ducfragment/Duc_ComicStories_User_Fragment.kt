package com.example.worldstory.duc.ducfragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.myapplication.databinding.FragmentDucComicStoriesUserBinding
import com.example.myapplication.databinding.ListCardStoriesLayoutBinding
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducactivity.DucSearchActivity
import com.example.worldstory.duc.ducadapter.Duc_Button_Adapter
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.duc.ducutils.createGridCardViewStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducviewmodel.DucGenreViewModel
import com.example.worldstory.duc.ducviewmodel.DucStoryViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucStoryViewModelFactory
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story
import com.example.worldstory.model.User
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Duc_ComicStories_User_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Duc_ComicStories_User_Fragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentDucComicStoriesUserBinding
    private lateinit var recyclerViewGenreButton: RecyclerView
    private lateinit var linearLayout: LinearLayout
    private var isText = false


    private val ducStoryViewModel: DucStoryViewModel by viewModels {
        DucStoryViewModelFactory(requireContext())
    }
    val ducGenreViewModel: DucGenreViewModel by viewModels {
        DucGenreViewModelFactory(requireContext())
    }

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
        binding = FragmentDucComicStoriesUserBinding.inflate(layoutInflater)
        val view = binding.root
        linearLayout = binding.linearLayoutFragmentComicStoryUser
        recyclerViewGenreButton = binding.rvButtonGenreComicStoriesUser
        var linearContainerGridCardStory =
            binding.linearContainerGridCardStoryFragmentComicStoriesUser
        ///////////////////////

        var listBindingGrid: List<ListCardStoriesLayoutBinding>
        ducStoryViewModel.genreAndStoriesByGenre.observe(viewLifecycleOwner, Observer { storiesByGenre ->

            createGridCardViewStory(
                requireContext(),
                inflater,
                linearContainerGridCardStory,
                storiesByGenre.first,
                storiesByGenre.second
            )

        })
        //button genre
        ducGenreViewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
            // tat hieu ung load
            binding.swipeRefreshComicStoriesUser.isRefreshing=false

            recyclerViewGenreButton.apply {
                layoutManager = GridLayoutManager(
                    view.context, 1, GridLayoutManager.HORIZONTAL, false
                )
                adapter = Duc_Button_Adapter(
                    requireContext(), ArrayList(genres), isText
                )
            }
            //xoa view con
            linearContainerGridCardStory.removeAllViews()
            for (genre in genres) {

                ducStoryViewModel.fetchGenreAndStoriesByGenre(genre, isText)
            }

        })

        //set image banner
        setImageBanner()
        //button search
        setConfigButton()


        //testDatabase()
//
        //Picasso.get().load(url2).fit().into(binding.imgTestComicUserFragment)

//       ---------------------------------------------------------------------------------
        // Inflate the layout for this fragment
        return view
    }

    private fun setImageBanner() {
        var imgURL: String =
            "https://drive.google.com/uc?id=1fPVkJqspSh0IQsQ_8teVapd5qf_q1ppV"
        var imgURL2: String =
            "https://drive.usercontent.google.com/download?id=11lEXSgF8HsX8BOyZ_Tek5Q_TI1FzWaBz&authuser=0"
        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel(imgURL))
        imageList.add(SlideModel(imgURL2))
        imageList.add(SlideModel(imgURL2))

        val imageSlider = binding.imgBannerComicUserstory
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        imageSlider.startSliding(3000)
    }





    fun toSearchActivity() {
        var intent = Intent(context, DucSearchActivity::class.java)
        intent.putExtra(getKeyIsText(requireContext()), isText)
        startActivity(intent)
    }

    fun setConfigButton() {
        var searchImgBtn = binding.searchButtonComicStoriesUser
        searchImgBtn.setOnClickListener {
            toSearchActivity()
        }
        binding.swipeRefreshComicStoriesUser.setOnRefreshListener{
            ducGenreViewModel.fetchGenres()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ComicStoriesUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Duc_ComicStories_User_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}