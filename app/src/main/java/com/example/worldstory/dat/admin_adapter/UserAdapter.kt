package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.model.User
import com.example.worldstory.dat.admin_viewholder.UserViewHolder
import com.example.worldstory.duc.SampleDataStory
import com.squareup.picasso.Picasso

class UserAdapter(private var userList: List<User>, private var color: Int) :
    RecyclerView.Adapter<UserViewHolder>(), Filterable {
    private var filteredList: List<User> = userList

    override fun getItemCount(): Int = filteredList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_row, parent, false)
        return UserViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredList[position]
        Picasso.get().load(user.imgAvatar).into(holder.avt_user_col)
        holder.column2.text = user.userID.toString()
        holder.column1.text ="Nickname: ${user.nickName}"
        holder.column3.text = user.createdDate
        holder.column4.text ="Username: ${user.userName}"
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(color)
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterByRole(role: Int) {
        filteredList = userList.filter { it.roleID == role }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence): FilterResults {
                val query: String = p0.toString()
                filteredList = if (query.isEmpty()) {
                    userList
                } else {
                    userList.filter {
                        it.nickName.contains(query, ignoreCase = true)
                                || it.userID.toString().contains(query, ignoreCase = true)
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList = p1.values as List<User>
                notifyDataSetChanged()
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(users: List<User>) {
        userList = users
        notifyDataSetChanged()
        Log.i("Observe", "da update")
    }

    fun getUser(position: Int): User {
        return filteredList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeUser(po: Int) {
        if (po >= 0 && po < userList.size) {
            (userList as MutableList).removeAt(po)
            notifyItemRemoved(po)
            notifyItemRangeChanged(po, userList.size)
        }
    }

}