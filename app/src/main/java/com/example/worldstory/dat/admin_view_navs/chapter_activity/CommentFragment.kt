package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCommentBinding
import com.example.worldstory.dat.admin_adapter.CommentAdapter
import com.example.worldstory.dat.admin_viewmodels.CommentViewModel
import com.example.worldstory.dat.admin_viewmodels.CommentViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


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

    private val DEBOUNCE_DELAY = 500L
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

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
        binding.lifecycleOwner = viewLifecycleOwner

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

        binding.searchCmt.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                runnable?.let { handler.removeCallbacks(it) }

                runnable= Runnable {
                    newText?.let{
                        commentAdapter.filter.filter(it)
                    }
                }

                handler.postDelayed(runnable!!, DEBOUNCE_DELAY)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                commentAdapter.filter.filter(query )
                hideKeyboard(binding.searchCmt)
                return true
            }
        })


        binding.searchCmt.setOnSearchClickListener {


            if (binding.headerTable.visibility == View.VISIBLE) {
                binding.headerTable.visibility = View.GONE
            }
        }
        binding.searchCmt.setOnCloseListener {
            if (binding.headerTable.visibility == View.GONE) {
                binding.headerTable.visibility = View.VISIBLE
            }
            commentAdapter.resetList()
            false
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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