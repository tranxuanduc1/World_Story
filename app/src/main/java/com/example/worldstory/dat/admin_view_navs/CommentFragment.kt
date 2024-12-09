package com.example.worldstory.dat.admin_view_navs

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCommentBinding
import com.example.worldstory.dat.admin_adapter.CommentAdapter
import com.example.worldstory.dat.admin_viewmodels.CommentViewModel
import com.example.worldstory.dat.admin_viewmodels.CommentViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Comment
import com.example.worldstory.model.User
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CommentFragment : Fragment() {

    companion object {
        fun newInstance(idStory: Int): CommentFragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putInt("idStory", idStory)
            fragment.arguments = args
            return fragment
        }
    }

    private var idStory: Int? = -1
    private lateinit var binding: FragmentCommentBinding
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var commentAdapter: CommentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        idStory = arguments?.getInt("idStory")
        commentViewModel = CommentViewModelFactory(
            DatabaseHelper(requireContext()),
            idStory
        ).create(CommentViewModel::class.java)
        binding.commentViewModel = commentViewModel
        binding.lifecycleOwner = this

        val barChart = binding.barChart

        updateBarChart(barChart)

        commentViewModel.comments.observe(viewLifecycleOwner) {
            updateBarChart(barChart)
        }


        // Khởi tạo RecyclerView
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

        commentAdapter = CommentAdapter(
            commentViewModel.commentUserMap.value?.toMutableMap()?: mutableMapOf(),
            commentViewModel.comments.value?.toMutableList()?: mutableListOf(),
            requireContext(),
            commentViewModel
        )
        binding.recycleView.adapter = commentAdapter
        // Gán adapter cho RecyclerView


        commentViewModel.comments.observe(viewLifecycleOwner)
        {

            commentAdapter.updateList(
                commentViewModel.commentUserMap.value?.toMutableMap()?: mutableMapOf(),
                commentViewModel.comments.value?.toMutableList()?: mutableListOf()
            )

            updateBarChart(barChart)
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateBarChart(barChart: BarChart) {


        val barChart = binding.barChart

        val quartComments =
            commentViewModel.groupCommentsByQuarter(commentViewModel.comments.value ?: emptyList())


        val barEntries = quartComments.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        // Tạo BarDataSet và cấu hình
        val barDataSet = BarDataSet(barEntries, "Số lượng bình luận theo quý")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)

        // Tạo BarData và hiển thị lên BarChart
        val barData = BarData(barDataSet)
        barChart.data = barData

        // Cấu hình biểu đồ
        barChart.description.text = "Số lượng bình luận"
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(quartComments.keys.toList())
        barChart.xAxis.granularity = 1f

        barChart.setScaleEnabled(true)
        barChart.setPinchZoom(true)
        barChart.setScaleMinima(1f, 1f)

        barChart.invalidate() // Làm mới biểu đồ
    }


}