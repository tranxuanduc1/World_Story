package com.example.worldstory.view.ducactivity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityDucChapterMarkedBinding
import com.example.worldstory.duc.ducadapter.Duc_ChapterMarked_Adapter
import com.example.worldstory.duc.ducutils.SetItemDecorationForRecyclerView
import com.example.myapplication.R
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.view_models.ducviewmodel.DucChapterMarkViewModel
import com.example.worldstory.view_models.ducviewmodel.DucChapterViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucChapterMarkViewModelFactory
import com.example.worldstory.view_models.ducviewmodelfactory.DucChapterViewModelFactory

class DucChapterMarkedActivity : AppCompatActivity() {
    private val ducChapterMarkViewModel: DucChapterMarkViewModel by viewModels {
        DucChapterMarkViewModelFactory(this)
    }
    private val ducChapterViewModel: DucChapterViewModel by viewModels {
        DucChapterViewModelFactory(this)
    }

    private lateinit var binding: ActivityDucChapterMarkedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucChapterMarkedBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)

        setDataChapterMark()
        setConfigButton()
        setSwipeRefesh()
    }


    private fun setDataChapterMark() {
        ducChapterMarkViewModel.fetchComboChapterByUserSession()
        ducChapterMarkViewModel.comboChapter.observe(this, Observer { comboChapter ->
            //tat hieu ung load
            binding.swipeRefreshChapterMarked.isRefreshing=false

            callLog("chaptermark", comboChapter.toString())
            // thong bao khi khong co du lieu
            if (comboChapter.isEmpty()) {
                binding.txtNotiChapterMarked.visibility = View.VISIBLE
                binding.txtNotiChapterMarked.text = getString(R.string.dataNotFound)
            } else {
                binding.txtNotiChapterMarked.visibility = View.GONE
            }
            var adapterChapterMarked = Duc_ChapterMarked_Adapter(this, comboChapter)
            var itemdeco = SetItemDecorationForRecyclerView(0, 0, 0, 0)

            binding.rvChapterChapterMarked.apply {
                adapter = adapterChapterMarked
                layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(itemdeco)
            }
        })

    }

    private fun setConfigButton() {
        binding.btnBackChapterMarked.setOnClickListener {
            finish()
        }
    }

    private fun setSwipeRefesh() {
        binding.swipeRefreshChapterMarked.setOnRefreshListener{
            //chay lai du lieu
            ducChapterMarkViewModel.fetchComboChapterByUserSession()

        }
    }

}


