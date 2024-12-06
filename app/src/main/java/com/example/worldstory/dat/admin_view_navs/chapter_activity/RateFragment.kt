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
import com.example.worldstory.model_for_test.Rate
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.PieData



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


        val pieChart = view.findViewById<PieChart>(R.id.pieChart)

// Dữ liệu cho biểu đồ
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, "Red"))
        entries.add(PieEntry(30f, "Blue"))
        entries.add(PieEntry(20f, "Green"))
        entries.add(PieEntry(10f, "Yellow"))

// Tạo dataset và thiết lập các thuộc tính
        val dataSet = PieDataSet(entries, "Colors")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.sliceSpace = 3f // Khoảng cách giữa các phần

// Tạo PieData
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(android.graphics.Color.WHITE)

// Gắn dữ liệu vào biểu đồ
        pieChart.data = pieData
        pieChart.setUsePercentValues(true) // Hiển thị theo phần trăm
        pieChart.description.isEnabled = false // Tắt phần mô tả
        pieChart.animateY(1000) // Hiệu ứng
        pieChart.invalidate() // Cập nhật lại biểu đồ





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