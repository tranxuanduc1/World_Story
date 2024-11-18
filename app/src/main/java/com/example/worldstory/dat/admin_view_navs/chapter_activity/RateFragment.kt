package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_adapter.RateAdapter
import com.example.worldstory.dat.model_for_test.Rate


class RateFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rateAdapter: RateAdapter
    private val rateList = mutableListOf<Rate>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //nạp rates
        recyclerView = view.findViewById<RecyclerView>(R.id.rate_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        for (i in 1..20) {
            rateList.add(Rate("Lee Sin", 5, "12/09/2019"))
            rateList.add(Rate("Yasou", 1, "20/11/2024"))
            rateList.add(Rate("Garen", 3, "01/01/2034"))
        }
        rateAdapter = RateAdapter(rateList)
        recyclerView.adapter = rateAdapter
    }

}