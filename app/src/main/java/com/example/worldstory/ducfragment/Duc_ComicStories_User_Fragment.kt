package com.example.worldstory.ducfragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentDucComicStoriesUserBinding
import com.example.worldstory.ducactivity.DucSearchActivity
import com.example.worldstory.ducadapter.Duc_Button_Adapter
import com.example.worldstory.ducutils.createGridCardViewStory
import com.example.worldstory.ducutils.getKeyIsComic
import com.example.worldstory.ducviewmodel.DucGenreViewModel
import com.example.worldstory.ducviewmodel.DucStoryViewModel
import com.example.worldstory.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.ducviewmodelfactory.DucStoryViewModelFactory

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
    private var isComic = true


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

        ///////////////////////
        recyclerViewGenreButton.layoutManager = GridLayoutManager(
            view.context, 1, GridLayoutManager.HORIZONTAL, false
        )
        recyclerViewGenreButton.adapter = Duc_Button_Adapter(
            //requireContext(), ArrayList(genreViewModel.genres.value), isComic
            requireContext(), ArrayList(ducGenreViewModel.getAllGenres()), isComic
        )

        var searchImgBtn = binding.searchButtonComicStoriesUser

        searchImgBtn.setOnClickListener {
            toSearchActivity()
        }


        for (genre in ducGenreViewModel.getAllGenres()) {

            createGridCardViewStory(
                requireContext(),
                inflater,
                linearLayout,
                genre,
                ducStoryViewModel.getComicStoriesByGenre(genre)
            )
        }
//        genreViewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
//
//
//            for (i in genres.indices) {
//                if(requireContext() != null && inflater != null && genres[i] != null && storiesViewModel.getComicStoriesByGenre(genres[i]) != null)
//                {
//                    createGridCardViewStory(
//                        requireContext(),
//                        inflater,
//                        linearLayout,
//                        genres[i],
//                        storiesViewModel.getComicStoriesByGenre(genres[i])
//                    )
//                }
//
//
//            }
//
//        })
        ///////////////////////

//       ---------------------------------------------------------------------------------
        // Inflate the layout for this fragment
        return view
    }

    fun toSearchActivity() {
        var intent = Intent(context, DucSearchActivity::class.java)
        intent.putExtra(getKeyIsComic(requireContext()), isComic)
        startActivity(intent)
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