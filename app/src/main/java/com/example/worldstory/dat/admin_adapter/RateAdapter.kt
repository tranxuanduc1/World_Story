package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.RateViewHolder
import com.example.worldstory.dat.admin_viewmodels.RateViewModel
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Rate
import com.example.worldstory.model.User


class RateAdapter(
    private val userList: MutableList<User>,
    private val rateList:MutableList<Rate>,
    private val rateViewModel: RateViewModel,
    private val context: Context
) :
    RecyclerView.Adapter<RateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rate_item_row, parent, false)
        return RateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val user = userList[position]
        val rate=rateList[position]
        holder.c2.text = user.userName
        holder.c3.text = user.nickName
        holder.c1.text = user.userID.toString()
        holder.itemView.setOnLongClickListener {
            showPopupMewnu(holder.itemView, rate =rate )
            true
        }
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.Light_Beige))
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }


    override fun getItemCount(): Int = userList.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(newUserList: List<User>,newRateList: List<Rate>) {
        userList.clear()
        rateList.clear()
        userList.addAll(newUserList)
        rateList.addAll(newRateList)
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupMewnu(view: View, rate: Rate) {
        val popupMenu = PopupMenu(view.context, view)

        popupMenu.menuInflater.inflate(R.menu.stats_options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.del_item -> {
                    val dialog = AlertDialog.Builder(view.context)
                    dialog.setMessage("Có chắc muốn xóa ?")
                        .setPositiveButton("Đồng ý") { dialog, _ ->
                            rateViewModel.delete(rate = rate)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Hủy") { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialog.show()
                }
            }
            true
        }
        popupMenu.show()
    }


}
