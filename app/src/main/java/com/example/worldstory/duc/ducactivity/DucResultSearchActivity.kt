package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityDucResultSearchBinding
import com.example.worldstory.duc.ducadapter.Duc_CardStoryItem_Adapter
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducutils.getDataNotFound
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getKeyResultSearchInfo
import com.example.worldstory.duc.ducutils.getKeyTextQuery
import com.example.worldstory.duc.ducviewmodel.DucStoryViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucStoryViewModelFactory

class DucResultSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucResultSearchBinding
    private lateinit var dataList: ArrayList< DucStoryDataClass>
    private lateinit var textQuery: String
    private var isText : Boolean=false
    private val ducStoryViewModel: DucStoryViewModel by viewModels{
        DucStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityDucResultSearchBinding.inflate(layoutInflater)
        val view =binding.root
        enableEdgeToEdge()
        setContentView(view)
        //===============================
        if(isCheckLoad(getKeyResultSearchInfo(this))==false){return}

        loadData()
        setData()
        setConfigButton()


    }

    private fun setData() {
        var cardStoryAdapter= Duc_CardStoryItem_Adapter(this,ArrayList(ducStoryViewModel.getStoriesByQuery(textQuery,isText)))
        var recyclerView: RecyclerView=binding.recyclerStoriesResultSearch
        recyclerView.adapter=cardStoryAdapter
        recyclerView.layoutManager= GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false)
        //recyclerView.setHasFixedSize(true)
    }

    private fun setConfigButton() {
        binding.btnBackResultSearch.setOnClickListener{
            finish()
        }
    }

    private fun isCheckLoad(key: String): Boolean {
        return intent.hasExtra(key)
    }
    private fun loadData(){
        var bundle=intent.getBundleExtra(getKeyResultSearchInfo(this))
        if(bundle!=null)
        {
            isText=bundle.getBoolean(getKeyIsText(this))
            textQuery=bundle.getString(getKeyTextQuery(this))?: getDataNotFound(this)

        }
    }
}