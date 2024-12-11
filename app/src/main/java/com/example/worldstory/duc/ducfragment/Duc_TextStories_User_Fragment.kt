package com.example.worldstory.duc.ducfragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.worldstory.duc.ducactivity.DucSearchActivity
import com.example.myapplication.databinding.FragmentDucTextStoriesBinding
import com.example.worldstory.duc.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.duc.ducadapter.Duc_Button_Adapter
import com.example.worldstory.duc.ducadapter.Duc_CardStoryItem_Adapter
import com.example.worldstory.duc.ducadapter.Duc_HighScoreStory_Adapter
import com.example.worldstory.duc.ducadapter.Duc_UseCreatedStory_Adapter
import com.example.worldstory.duc.ducutils.SetItemDecorationForRecyclerView
import com.example.worldstory.duc.ducutils.createGridCardViewStory
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducviewmodel.DucGenreViewModel
import com.example.worldstory.duc.ducviewmodel.DucStoryViewModel
import com.example.worldstory.duc.ducviewmodel.DucUserViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucStoryViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucUserViewModelFactory
import com.example.worldstory.model.Story
import com.example.worldstory.model.User
import kotlin.collections.forEach
import kotlin.collections.getOrDefault
import kotlin.getValue

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Duc_TextStories_User_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Duc_TextStories_User_Fragment : Fragment() {
    private lateinit var binding: FragmentDucTextStoriesBinding
    private lateinit var recyclerViewGenreButton: RecyclerView
    private lateinit var linearLayout: LinearLayout
    private val ducStoryViewModel: DucStoryViewModel by viewModels {
        DucStoryViewModelFactory(requireContext())
    }
    private val ducGenreViewModel: DucGenreViewModel by viewModels {
        DucGenreViewModelFactory(requireContext())
    }
    private val ducUserViewModel: DucUserViewModel by viewModels {
        DucUserViewModelFactory(requireContext())
    }
    private var isText = true


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
        //val view=inflater.inflate(R.layout.fragment_text_stories, container, false)
        binding = FragmentDucTextStoriesBinding.inflate(layoutInflater)
        val view = binding.root
//--------------------------------------
        linearLayout = binding.linearLayoutFragmentTextStoryUser
        recyclerViewGenreButton = binding.rvButtonGenreTextStoriesUser
        var linearContainerGridCardStory =
            binding.linearContainerGridCardStoryFragmentTextStoriesUser


        ///////////////////////

        ducStoryViewModel.genreAndStoriesByGenre.observe(
            viewLifecycleOwner,
            Observer { storiesByGenre ->
                var numStories = 6
                numStories =
                    if (storiesByGenre.second.size >= 6)
                        numStories
                    else
                        storiesByGenre.second.size
                createGridCardViewStory(
                    requireContext(), inflater,
                    linearContainerGridCardStory,
                    storiesByGenre.first,
                    storiesByGenre.second.take(numStories),
                    isText
                )

            })

        //button genre
        ducGenreViewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
            // tat hieu ung load
            binding.swipeRefreshTextStoriesUser.isRefreshing = false

            recyclerViewGenreButton.layoutManager = GridLayoutManager(
                view.context,
                1,
                GridLayoutManager.HORIZONTAL, false
            )
            recyclerViewGenreButton.adapter = Duc_Button_Adapter(
                requireContext(), ArrayList(genres),
                isText
            )
            //xoa view con
            linearContainerGridCardStory.removeAllViews()
            for (genre in genres) {
                ducStoryViewModel.fetchGenreAndStoriesByGenre(genre, isText)
            }
        })
        //tao truyen hot,user hot, high score stories
        ducStoryViewModel.fetchStoriesIsText(isText)
        ducStoryViewModel.storiesIsText.observe(viewLifecycleOwner, Observer { stories ->
            setHotStoies(stories)
            setHighScoreStoies(stories)
            setHotUser(stories)
            //set image banner
            setImageBanner(stories)
        })

        //button search
        setConfigButton()


        // Inflate the layout for this fragment
        return view
    }

    private fun setConfigButton() {
        var searchImgBtn = binding.searchButtonTextStoriesUser
        searchImgBtn.setOnClickListener {
            toSearchActivity()
        }
        binding.swipeRefreshTextStoriesUser.setOnRefreshListener {
            ducGenreViewModel.fetchGenres()
        }
    }

    fun toSearchActivity() {
        var intent = Intent(context, DucSearchActivity::class.java)
        intent.putExtra(getKeyIsText(requireContext()), isText)
        startActivity(intent)
    }
    private fun getHotUsers(
        users: List<User>,
        numUsers: Int,
        stories: List<Story>
    ): List<Pair<User, Int>> {
        var newMap = mutableMapOf<Int, Int>()
        stories.forEach {
            //lay duoc so lan cac user tao truyen
            newMap[it.userID] = newMap.getOrDefault(it.userID, 0) + 1
        }
        var newMapB = mutableMapOf<User, Int>()
        users.forEach {
            newMapB[it] = newMap.getOrDefault(it.userID, 0)
        }
        var newPairList = newMapB.toList().sortedByDescending { it.second }
        newPairList = if (newPairList.size >= numUsers) newPairList.take(numUsers) else newPairList
        return newPairList
    }


    private fun setHotStoies(stories: List<Story>) {
        var numberStoryShow = 6
        // lay 6 phan tu
        var limitStories = stories.take(numberStoryShow)
        var adapterHotStories = Duc_CardStoryItem_Adapter(requireContext(), ArrayList(limitStories))
        binding.rvHotStoriesTextStoriesUser.apply {
            adapter = adapterHotStories
            layoutManager =
                GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setHighScoreStoies(stories: List<Story>) {
        var numberStoryShow = 5
        var numCol = 1
        var numSpace = 20
        val itemDeco = SetItemDecorationForRecyclerView(0, 10, 5, 5)

        //lay danh sach story tuong tac nhieu nhat
        ducStoryViewModel.fetchComboHighScoreStories(stories)
        ducStoryViewModel.comboHighScoreStories.observe(viewLifecycleOwner, Observer { combo ->
            //lay 5 phan tu co diem cao nhat

            var limitStories = combo.sortedByDescending { it.numRating }.take(numberStoryShow)
            var adapterHighScoreStories =
                Duc_HighScoreStory_Adapter(requireContext(), ArrayList(limitStories))

            binding.rvHighScoreStoriesTextStoriesUser.apply {
                adapter = adapterHighScoreStories
                layoutManager =
                    GridLayoutManager(context, numCol, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(itemDeco)
                setHasFixedSize(true)

            }
        })


    }

    private fun setHotUser(stories: List<Story>) {
        ducUserViewModel.fetchAuthorUser()
        //tao user hot
        ducUserViewModel.userAuthor.observe(viewLifecycleOwner, Observer { users ->
            var numUsers = 6
            var topUser = getHotUsers(users, numUsers, stories)
            var limitUser = if (users.size >= 5) users.take(numUsers) else users
            var adapterAuthorUser = Duc_UseCreatedStory_Adapter(requireContext(), topUser, isText)
            var itemDeco = SetItemDecorationForRecyclerView(0, 5, 1, 1)
            binding.rvHotUsersTextStoriesUser.apply {
                adapter = adapterAuthorUser
                layoutManager =
                    GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(itemDeco)
            }
        })
    }
    private fun setImageBanner(stories: List<Story>) {
        var random = java.util.Random()
        var numImage = 3
        val imageListBanner = ArrayList<SlideModel>() // Create image list
        val storyListBanner = ArrayList<Story>()
        //lay 3 truyen ngau nhien
        for (i in 1..numImage) {
            var ran = random.nextInt(stories.size)
            var story = stories[ran]
            imageListBanner.add(SlideModel(story.bgImgUrl))
            storyListBanner.add(story)
        }

        //gan 3 truyen ngau nhien vao imageslider
        val imageSlider = binding.imgBannerTextUserstory
        imageSlider.setImageList(imageListBanner, ScaleTypes.CENTER_CROP)
        imageSlider.startSliding(3000)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {

            }

            override fun onItemSelected(position: Int) {
                requireContext().toActivity(
                    DucStoryOverviewActivity::class.java,
                    getKeyStoryInfo(requireContext()),
                    storyListBanner[position]
                )
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TextStoriesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Duc_TextStories_User_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}