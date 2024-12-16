package com.example.worldstory.view.ducactivity

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucSearchBinding
import com.example.worldstory.duc.ducadapter.Duc_ListSearch_ArrayAdapter
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getKeyResultSearchInfo
import com.example.worldstory.duc.ducutils.getKeyTextQuery

import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.view_models.ducviewmodel.DucStoryViewModel
import com.example.worldstory.view_models.ducviewmodelfactory.DucStoryViewModelFactory
import com.example.worldstory.data.model.Story

class DucSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucSearchBinding
    private lateinit var searchView: SearchView
    private lateinit var listViewSearchResults: ListView
    private lateinit var searchAdapter: Duc_ListSearch_ArrayAdapter
    private lateinit var dataList: ArrayList<Story>
    private val ducStoryViewModel: DucStoryViewModel by viewModels {
        DucStoryViewModelFactory(this)
    }
    private var isText: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucSearchBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        //----------------
        var key = getKeyIsText(this)
        // neu khong biet la tim kiem comic hay text
        if (checkLoadData(key) == false) {
            return
        }
        isText = intent.getBooleanExtra(key, false)

        searchView = binding.searchViewSearch
        listViewSearchResults = binding.listViewSearch

        ducStoryViewModel.stories.observe(this, Observer { stories ->
            dataList = ArrayList(ducStoryViewModel.getStoriesByIsText(isText))
            searchAdapter = Duc_ListSearch_ArrayAdapter(
                view.context, R.layout.list_item_search_layout, dataList
            )
            listViewSearchResults.adapter = searchAdapter
            setSearchViewListener()
        })
        searchView.setOnCloseListener {
            searchView.clearFocus()
            listViewSearchResults.visibility = View.GONE
            true
        }

    }

    private fun setSearchViewListener() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                listViewSearchResults.visibility = View.GONE
                if (!query.isNullOrEmpty()) {
                    toResultSearchActivity(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    listViewSearchResults.visibility = View.GONE


                } else {
                    listViewSearchResults.visibility = View.VISIBLE
                    searchAdapter.filter.filter(newText)
                    listViewSearchResults.invalidate()
                    // updateListViewHeight(listViewSearchResults)
                }

                return true;
            }
        })
    }

    private fun checkLoadData(key: String): Boolean {
        return intent.hasExtra(key)
    }

    fun updateListViewHeight(listView: ListView) {
        val adapter = listView.adapter ?: return

        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(
                View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (adapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

    fun toResultSearchActivity(textQuery: String) {
        var bundle = createBundle(textQuery)
        toActivity(DucResultSearchActivity::class.java, getKeyResultSearchInfo(this), bundle)
    }

    fun createBundle(textQuery: String): Bundle {
        var bundle = Bundle()
        bundle.putBoolean(getKeyIsText(this), isText)
        bundle.putString(getKeyTextQuery(this), textQuery)
        return bundle
    }
}