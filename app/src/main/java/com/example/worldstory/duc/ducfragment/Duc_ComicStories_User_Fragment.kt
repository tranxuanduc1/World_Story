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
            recyclerViewGenreButton.apply {
                layoutManager = GridLayoutManager(
                    view.context, 1, GridLayoutManager.HORIZONTAL, false
                )
                adapter = Duc_Button_Adapter(
                    requireContext(), ArrayList(genres), isText
                )
            }
            for (genre in genres) {

                ducStoryViewModel.fetchGenreAndStoriesByGenre(genre, isText)
            }


            //card view stories
//            ducStoryViewModel.stories.observe(viewLifecycleOwner, Observer { stories ->
//                for (genre in genres) {
//                    createGridCardViewStory(
//                        requireContext(),
//                        inflater,
//                        linearLayout,
//                        genre,
//                        ducStoryViewModel.getStoriesByGenre(genre.genreID ?: 1, isText)
//                    )
//
//                }
//                //set image banner
//                setImageBanner()
//            })
        })

        //set image banner
        setImageBanner()
        //button search
        setConfigButton()


        //testDatabase()


//       ---------------------------------------------------------------------------------
        // Inflate the layout for this fragment
        return view
    }

    private fun setImageBanner() {
        var imgURL: String =
            "https://storage-ct.lrclib.net/file/cuutruyen/uploads/manga/2478/cover/processed-4079ee6ed3b108490e33fca63589c35e.jpg"
        var imgURL2: String =
            "https://drive.usercontent.google.com/download?id=11lEXSgF8HsX8BOyZ_Tek5Q_TI1FzWaBz&authuser=0"
        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel(imgURL2))
        imageList.add(SlideModel(imgURL2))
        imageList.add(SlideModel(imgURL2))

        val imageSlider = binding.imgTestComicUserstory
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        imageSlider.startSliding(3000)
    }



    fun addStoryWithDetails(story: Story, dbHelper: DatabaseHelper) {
        val p1 =
            "https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467623087_987608846509607_7464989657181579078_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=127cfc&_nc_ohc=PJhBdDIh8ngQ7kNvgFXOx3O&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=AFd7aN9gB5bxoYEwwsXIv2f&oh=00_AYD0Z_URAyRNGfnjU-LURgA-2gVde9_z-mv0bGN_9BaT5g&oe=67422259"
        val p2 =
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467723438_987608853176273_4668882966082102865_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=127cfc&_nc_ohc=YWlZYUbYXkwQ7kNvgE3jYCO&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=AGDIEQX_cb1_fgYK36qvGVI&oh=00_AYCdk4vBlHeH8OamHhgPsh8Krue4z4n87eTw0ze93H0qkg&oe=674228AA"
        val p3 =
            "https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467668063_987608843176274_2859379085125334494_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_ohc=yXXD9A77qecQ7kNvgF_d8YC&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=APlheJAZPnckmNTfHmSXxw1&oh=00_AYA_aNaPZsNIj4d7_m7buS-uLLEZqlrXLFmCT6tjuBpVzg&oe=6742542B"
        val p4 =
            "https://scontent.fhan3-4.fna.fbcdn.net/v/t39.30808-6/467593664_987608919842933_5614161165862365298_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=M890QOlTAdgQ7kNvgGjskoW&_nc_zt=23&_nc_ht=scontent.fhan3-4.fna&_nc_gid=ABNzVvhlzRKGC_pZhF3yrl4&oh=00_AYDMJu16KD0Bab_Mw-oiuajQDLnUI_f6W7WFhcMHoZt7sg&oe=67423E82"
        val p5 =
            "https://scontent.fhan3-4.fna.fbcdn.net/v/t39.30808-6/467600225_987608923176266_4457027657328368414_n.jpg?_nc_cat=106&ccb=1-7&_nc_sid=127cfc&_nc_ohc=DVp19tJB7wYQ7kNvgEPu0iF&_nc_zt=23&_nc_ht=scontent.fhan3-4.fna&_nc_gid=Apt9d8aT9XV4egL6li5M-yW&oh=00_AYBUuZIL6k6Lrw2KoBPCGUFMf8MUXGL76a5nS48oD0pmJA&oe=67424E4A"

        // Insert the story
        val storyId = dbHelper.insertStory(story)

        if (storyId != -1L) {
            // Add 4 chapters for the story
            for (i in 1..4) {
                val chapter =
                    Chapter(null, "Chapter $i of ${story.title}", dateTimeNow(), storyId.toInt())
                val chapterId = dbHelper.insertChapter(chapter)

                if (chapterId != -1L) {
                    // Add 4 paragraphs for each chapter
                    if (story.isTextStory == 0) {

//                            var image = Image(
//                                null,
//                                SampleDataStory.getExampleImgURLParagraph(),
//                                j,
//                                chapterId.toInt()
//                            )
                        var image1 = Image(null, p1, 1, chapterId.toInt())
                        var image2 = Image(null, p2, 2, chapterId.toInt())
                        var image3 = Image(null, p3, 3, chapterId.toInt())
                        var image4 = Image(null, p4, 4, chapterId.toInt())
                        var image5 = Image(null, p5, 5, chapterId.toInt())

                        dbHelper.insertImage(image1)
                        dbHelper.insertImage(image2)
                        dbHelper.insertImage(image3)
                        dbHelper.insertImage(image4)
                        dbHelper.insertImage(image5)

                    }
                    for (j in 1..4) {

                        if (story.isTextStory == 0) {

//                            var image = Image(
//                                null,
//                                SampleDataStory.getExampleImgURLParagraph(),
//                                j,
//                                chapterId.toInt()
//                            )
//                            var image1 = Image(null, p1, 1,chapterId.toInt())
//                            var image2 = Image(null, p2, 2,chapterId.toInt())
//                            var image3 = Image(null, p3, 3,chapterId.toInt())
//                            var image4 = Image(null, p4, 4,chapterId.toInt())
//                            var image5 = Image(null, p5, 5,chapterId.toInt())
//
//                            dbHelper.insertImage(image1)
//                            dbHelper.insertImage(image2)
//                            dbHelper.insertImage(image3)
//                            dbHelper.insertImage(image4)
//                            dbHelper.insertImage(image5)

                        } else {
                            var paragraph = Paragraph(
                                null,
                                getLoremIpsumLong(),
                                j,
                                chapterId.toInt()
                            )
                            dbHelper.insertParagraph(paragraph)

                        }

                    }
                }
            }

            // Add 2 comments for the story
            for (i in 1..2) {
                val comment = Comment(
                    null,
                    "Comment $i on ${story.title}",
                    dateTimeNow(),
                    userId = 1, // Assuming user ID 1 for simplicity
                    storyId = storyId.toInt()
                )
                dbHelper.insertComment(comment)
            }
        }
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