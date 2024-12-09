package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRateLayoutBinding
import com.example.worldstory.dat.admin_adapter.RateAdapter
import com.example.worldstory.dat.admin_viewmodels.RateViewModel
import com.example.worldstory.dat.admin_viewmodels.RateViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Rate
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


class RateFragment : Fragment() {

    companion object {
        fun newInstance(idStory: Int): RateFragment {
            val fragment = RateFragment()
            val args = Bundle()
            args.putInt("idStory1", idStory)
            fragment.arguments = args
            return fragment
        }
    }


    private var idStory: Int? = -1
    private lateinit var rateAdapter: RateAdapter
    private val rateRatioList = mutableListOf<Float>()
    private lateinit var rateViewModel: RateViewModel
    private lateinit var binding: FragmentRateLayoutBinding


    private val maxScore = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRateLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        idStory = arguments?.getInt("idStory1")
        rateViewModel = RateViewModelFactory(
            DatabaseHelper(requireContext()),
            idStory
        ).create(RateViewModel::class.java)

        binding.rateViewModel = rateViewModel
        binding.lifecycleOwner = this

        Log.w("id", idStory.toString())
        val pieChart = binding.pieChart

        updatePieChart(pieChart)

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Toast.makeText(requireContext(), "No slice selected", Toast.LENGTH_SHORT).show()
            }

            @SuppressLint("SetTextI18n")
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    val label = e.label
                    for (i in 1 until maxScore + 1) {
                        if (label.contains("$i")) {
                            rateViewModel.setRateListBtScore(i)
                            binding.sao.text = "$i sao"
                            binding.numberUserRate.text = "Số người đánh giá $i sao:"
                            break
                        }
                    }
                }
            }
        })


        //nạp rates
        binding.rateList.layoutManager = LinearLayoutManager(requireContext())

        rateAdapter = RateAdapter(
            rateViewModel.users.value?.toMutableList() ?: mutableListOf(),
            rateViewModel.rateListByScore.value?.toMutableList() ?: mutableListOf(),
            rateViewModel,
            requireContext()
        )

        binding.rateList.adapter = rateAdapter

        rateViewModel.rateList.observe(viewLifecycleOwner) {
            Log.w("del1",rateViewModel.rateList.value?.size.toString())
            updatePieChart(pieChart)
        }

        rateViewModel.rateListByScore.observe(viewLifecycleOwner) {
            rateAdapter.update(
                rateViewModel.users.value?.toMutableList() ?: mutableListOf(),
                rateViewModel.rateListByScore.value?.toMutableList() ?: mutableListOf()
            )
        }

    }

    fun updatePieChart(pieChart: PieChart) {
rateRatioList.clear()
        // Dữ liệu cho biểu đồ
        val entries = ArrayList<PieEntry>()

        for (i in 1 until maxScore + 1) {
            rateRatioList.add(rateViewModel.getRatio(i))

            entries.add(PieEntry(rateRatioList[i - 1], "$i sao"))
        }

        // Tạo dataset và thiết lập các thuộc tính
        val dataSet = PieDataSet(entries, "Colors")
        dataSet.colors = listOf(
            Color.RED,
            ContextCompat.getColor(requireContext(), R.color.flame),
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE
        )
        dataSet.sliceSpace = 3f // Khoảng cách giữa các phần

        // Tạo PieData
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.WHITE)


        // Gắn dữ liệu vào biểu đồ
        pieChart.data = pieData
        pieChart.setUsePercentValues(true) // Hiển thị theo phần trăm
        pieChart.description.isEnabled = false // Tắt phần mô tả
        pieChart.setDrawEntryLabels(false)
        pieChart.isHighlightPerTapEnabled = true  // Cho phép chọn slice khi bấm
        pieChart.setTouchEnabled(true)           // Cho phép tương tác với PieChart
        pieChart.animateY(1000) // Hiệu ứng
        pieChart.invalidate() // Cập nhật lại biểu đồ
    }
}