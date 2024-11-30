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

    private fun testDatabase() {
        var dataHelper: DatabaseHelper = DatabaseHelper(requireContext())
        dataHelper.insertRole(Role(null, "Admin"))
        dataHelper.insertRole(Role(null, "Moderator"))
        dataHelper.insertRole(Role(null, "Member"))
        dataHelper.insertRole(Role(null, "Guest"))

        dataHelper.insertUser(
            User(
                null,
                "user1",
                "hashedPassword1",
                SampleDataStory.getExampleImgURL(),
                "nickname1",
                1,
                dateTimeNow
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user2",
                "hashedPassword2",
                SampleDataStory.getExampleImgURL(),
                "nickname2",
                2,
                dateTimeNow
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user3",
                "hashedPassword3",
                SampleDataStory.getExampleImgURL(),
                "nickname3",
                3,
                dateTimeNow
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user4",
                "hashedPassword4",
                SampleDataStory.getExampleImgURL(),
                "nickname4",
                4,
                dateTimeNow
            )
        )

        dataHelper.insertGenre(Genre(null, "Action", 1))
        dataHelper.insertGenre(Genre(null, "Romance", 1))
        dataHelper.insertGenre(Genre(null, "Horror", 1))
        dataHelper.insertGenre(Genre(null, "Fantasy", 1))


        val imgUrlListString = arrayOf(
            "https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467637825_987540789849746_543458540327297711_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=iQOcKkq2qUYQ7kNvgEgyvuo&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=Aym2wPdWobnahsnjGHBJ8Oe&oh=00_AYDvl3AyQ-c3GFb3FLj7XK2t_c8cj8bY6POC_kkaENEi1A&oe=67420244",
            "https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467577331_987540779849747_3591926939402667045_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=127cfc&_nc_ohc=NhyibJOpEmAQ7kNvgEAGM7r&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=AnVmve5NRl4OrLjx-d8EHne&oh=00_AYCuRMR1pjI8KRGlcie1RnAycQoSrQ86nppSJgvOwGmXyA&oe=67423357",
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467696713_987540776516414_3219174740401399891_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=127cfc&_nc_ohc=_x1oBN8vZ08Q7kNvgFg-7B2&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=A3p-wnaOT9q7gXqwrdGy3Hv&oh=00_AYA7bZIXbUy0YH5EeHr0a6WQYhTrJy3b_FOKLg-ytMTFAw&oe=674201D0",
            "https://scontent.fhan3-2.fna.fbcdn.net/v/t39.30808-6/467680359_987540853183073_3431101542059115151_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=127cfc&_nc_ohc=FZ3t1GKjXcsQ7kNvgHIplgn&_nc_zt=23&_nc_ht=scontent.fhan3-2.fna&_nc_gid=AE6bREnqPK2sD6wjLNDbenN&oh=00_AYActiD5cuUpUFkWB7k4un7vUW4CLgYUSmVn7EW_rTf0Dg&oe=6741FCEC",
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467687540_987540869849738_578832179973704373_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=hE8KZhy98R4Q7kNvgEmR-6G&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=Ayo1347oHOkojUjtC04Fzy3&oh=00_AYDMlqHAUr8wUxWQ2FOTTQYIeethR-rT4v3agvmwvodS0w&oe=6741FFC7",
            "https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467606655_987540883183070_3596325563253202205_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_ohc=nCgzSeoEgCsQ7kNvgGhirLp&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=AYuMFUCkmXPaL0AyAlRd1Ut&oh=00_AYDbunSnUrEHDr9tLJaEBe5O3_deeWLGlx4ywpizou7Eow&oe=674205DF",
            "https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467639302_987540783183080_2145138456043692649_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=PYodwl72Ig4Q7kNvgHdlunn&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=ARqyVa_1UAjU50lR0wzoyo1&oh=00_AYBQLZBtN5AE_I-ljfAe_ca1xhd1IA8nykuVvQ4MMmo5gQ&oe=67420FFC",
            "https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467618820_987540786516413_9022671729236654001_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=wcRr4uG_frQQ7kNvgG7AO3Q&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=ACWt9Nxlog48s7vciMKLvlm&oh=00_AYCFt2WZkt3Qy8rXYq0fnq2KdcuMdRdl7Qr9prRr8E71gw&oe=67420403",


            )

        for (i in 1..5) {
            val story = Story(
                storyID = null,
                title = "Story $i",
                description = "Description of Story $i",
                imgUrl = imgUrlListString[i],
                bgImgUrl = imgUrlListString[i],
                author = "Author $i",
                createdDate = dateTimeNow,
                isTextStory = 0,
                score = 4.0f,
                userID = 1 // Assuming user ID 1 created the stories
            )
            addStoryWithDetails(story, dataHelper)
        }
        // Add 4 stories with details
        for (i in 6..10) {
            val story = Story(
                storyID = null,
                title = "Story $i",
                description = "Description of Story $i",
                imgUrl = SampleDataStory.getExampleImgURL(),
                bgImgUrl = SampleDataStory.getExampleImgURL(),
                author = "Author $i",
                createdDate = dateTimeNow,
                isTextStory = 1,
                score = 4.0f,
                userID = 1 // Assuming user ID 1 created the stories
            )
            addStoryWithDetails(story, dataHelper)
        }
        dataHelper.insertStoryGenre(storyId = 1, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 1, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 2, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 2, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 3, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 4, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 5, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 5, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 6, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 6, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 7, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 7, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 7, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 8, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 8, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 8, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 9, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 10, genreId = 2)
        // Insert 4 rows for the UserLoveStory table
        dataHelper.insertUserLoveStory(1, 1)
        dataHelper.insertUserLoveStory(2, 2)
        dataHelper.insertUserLoveStory(3, 3)
        dataHelper.insertUserLoveStory(4, 4)

        dataHelper.insertChapterMark(1, 1)
        dataHelper.insertChapterMark(2, 2)
        dataHelper.insertChapterMark(3, 3)
        dataHelper.insertChapterMark(4, 4)

        dataHelper.insertChapterHistory(1, 1)
        dataHelper.insertChapterHistory(2, 2)
        dataHelper.insertChapterHistory(3, 3)
        dataHelper.insertChapterHistory(4, 4)
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
                    Chapter(null, "Chapter $i of ${story.title}", dateTimeNow, storyId.toInt())
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
                    dateTimeNow,
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