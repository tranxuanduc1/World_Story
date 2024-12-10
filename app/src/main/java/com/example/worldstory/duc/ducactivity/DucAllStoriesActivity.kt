package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucAllStoriesBinding
import com.example.worldstory.duc.ducadapter.Duc_CardStoryItem_Adapter
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducviewmodel.DucStoryViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucStoryViewModelFactory

class DucAllStoriesActivity : AppCompatActivity() {

    private val ducStoryViewModel: DucStoryViewModel by viewModels{
        DucStoryViewModelFactory(this)
    }
    private lateinit var binding: ActivityDucAllStoriesBinding

    private var isText=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDucAllStoriesBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)


        isText= intent.getBooleanExtra(getKeyIsText(this),false)
        ducStoryViewModel.fetchStoriesIsText(isText)
        ducStoryViewModel.storiesIsText.observe(this, Observer{
            stories->
            var numCol=3
            var adapterstoies= Duc_CardStoryItem_Adapter(this, ArrayList(stories))
            binding.rvStoriesAllStories.apply {
                adapter=adapterstoies
                layoutManager= GridLayoutManager(context,numCol, LinearLayoutManager.VERTICAL,false)
            }
        })

        binding.btnBackAllStories.setOnClickListener{
            finish()
        }

    }
}