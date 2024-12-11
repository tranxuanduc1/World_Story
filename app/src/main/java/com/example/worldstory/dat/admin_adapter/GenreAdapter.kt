package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.GenreViewHolder
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Rate

class GenreAdapter(
    private val genreViewModel: GenreViewModel,
    private var genreList: List<Genre>,
    private var color: Int
) :
    RecyclerView.Adapter<GenreViewHolder>(), Filterable {
    private var filteredList: List<Genre> = genreList
    override fun getItemCount(): Int = filteredList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_item_row, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = filteredList[position]
        val sum = genreViewModel.sumStoryByGenre(genre.genreID ?: 0)
        holder.column1.text = genre.genreID.toString()
        holder.column2.text = genre.genreName
        holder.column3.text = sum.toString()
        holder.itemView.setOnLongClickListener {
            showPopupMewnu(holder.itemView, genre)
            true
        }
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(color)
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence): FilterResults {
                var query: String = p0.toString()
                filteredList = if (query.isEmpty()) {
                    genreList
                } else {
                    genreList.filter {
                        it.genreName.contains(query, ignoreCase = true)
                                || it.genreID.toString().contains(query, ignoreCase = true)
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList = p1.values as List<Genre>
                notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(genre: List<Genre>) {
        genreList = genre
        notifyDataSetChanged()
    }

    fun getGenre(p: Int): Genre {
        return filteredList[p]
    }

    fun remove(p: Int) {
        if (p >= 0 && p < genreList.size) {
            (genreList as MutableList).removeAt(p)
            notifyItemRemoved(p)
            notifyItemRangeChanged(p, genreList.size)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupMewnu(view: View, genre: Genre) {
        val popupMenu = PopupMenu(view.context, view)

        popupMenu.menuInflater.inflate(R.menu.edit_option, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_item -> {
                    val editText = view.findViewById<EditText>(R.id.ca_column2_edit)
                    val textView2 = view.findViewById<TextView>(R.id.ca_column2)
                    val textView3 = view.findViewById<TextView>(R.id.ca_column3)
                    val acceptBtn = view.findViewById<ImageButton>(R.id.accept_change)
                    val cancelBtn = view.findViewById<ImageButton>(R.id.cancel_change)

                    val open = fun(): Boolean{
                        editText.visibility = View.VISIBLE
                        acceptBtn.visibility = View.VISIBLE
                        cancelBtn.visibility = View.VISIBLE
                        textView2.visibility = View.GONE
                        textView3.visibility = View.GONE
                        return true
                    }
                    val close=fun():Boolean{
                        editText.visibility = View.GONE
                        acceptBtn.visibility = View.GONE
                        cancelBtn.visibility = View.GONE
                        textView2.visibility = View.VISIBLE
                        textView3.visibility = View.VISIBLE
                        return true
                    }
                    open()



                    val dialog = AlertDialog.Builder(view.context)
                    dialog.setMessage("Đổi tên")

                        .setPositiveButton("Đồng ý") { dialog, _ ->
                            val name =
                                editText.text.toString()
                            val editedGenre = Genre(
                                genreID = genre.genreID,
                                genreName = name,
                                userID = genre.userID
                            )
                            genreViewModel.updateGenre(editedGenre)
                            close()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Hủy") { dialog, _ ->
                            close()
                            dialog.dismiss()
                        }

                    acceptBtn.setOnClickListener {
                        dialog.show()
                    }
                    cancelBtn.setOnClickListener {
                        close()
                    }

                }
            }
            true
        }
        popupMenu.show()
    }


}