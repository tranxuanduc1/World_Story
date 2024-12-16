package com.example.worldstory.view.ducactivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.ActivityDucStoriesByGenreBinding
import com.example.worldstory.duc.ducadapter.Duc_CardStoryItem_Adapter
import com.example.worldstory.duc.ducutils.getDataNotFound
import com.example.worldstory.duc.ducutils.getExampleGenre
import com.example.worldstory.duc.ducutils.getKeyGenreInfo
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getKeyStoriesByGenre
import com.example.worldstory.duc.ducutils.getTextDataNotFound
import com.example.worldstory.view_models.ducviewmodel.DucStoryViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucStoryViewModelFactory
import com.example.worldstory.data.model.Genre

class DucStoriesByGenreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucStoriesByGenreBinding
    private val ducStoryViewModel: DucStoryViewModel by viewModels{
        DucStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDucStoriesByGenreBinding.inflate(layoutInflater)
        val view =binding.root
        enableEdgeToEdge()
        setContentView(view)
        setButtonWithOutData()
        //----------------------

        var keyData= getKeyStoriesByGenre(this)
        if(checkLoadData(keyData)){
            loadData(keyData)

        }else{
            Toast.makeText(this, getDataNotFound(this), Toast.LENGTH_LONG).show()
        }

    }

    private fun loadData(key: String) {
        var bundle= intent.getBundleExtra(key)
        if(bundle is Bundle){
            var isText =bundle.getBoolean(getKeyIsText(this))
            var genreInfo: Genre? = bundle.getParcelable(
                getKeyGenreInfo(this)
            )
            binding.txtTitleGenreStoriesByGenre.text=genreInfo?.genreName ?: getTextDataNotFound(this)
            setCardStories(isText,genreInfo?: getExampleGenre(this))
        }
    }

    fun checkLoadData(key: String): Boolean{

        return intent.hasExtra(key)
    }
    private fun setCardStories(isText: Boolean, genre: Genre) {
        ducStoryViewModel.fetchGenreAndStoriesByGenre(genre,isText)
        ducStoryViewModel.genreAndStoriesByGenre.observe(this, Observer{
            storiesByGenre ->

            var dataList=ArrayList( storiesByGenre.second)
            var cardAdapter = Duc_CardStoryItem_Adapter(this, dataList)
            binding.recyclerStoryStoriesByGenre.apply {
                adapter=cardAdapter
                layoutManager= GridLayoutManager(context,3,
                    GridLayoutManager.VERTICAL,false)
            }

        })

    }

    private fun setButtonWithOutData() {
        binding.btnBackStoriesByGenre.setOnClickListener{
            finish()
        }
    }

}