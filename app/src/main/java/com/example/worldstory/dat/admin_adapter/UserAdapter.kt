package com.example.worldstory.dat.admin_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.model_for_test.User
import com.example.worldstory.dat.admin_viewholder.UserViewHolder

class UserAdapter(private var userList: List<User>, private var color:Int) :
    RecyclerView.Adapter<UserViewHolder>(), Filterable {
    private var filteredList: List<User> = userList

    override fun getItemCount(): Int = filteredList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_row, parent, false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredList[position]
        holder.column1.text = user.name
        holder.column2.text = user.id
        holder.column3.text = user.role.toString()
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(color)}
        else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }
    fun filterByRole(role:String){
        filteredList=userList.filter { it.role.toString().contains(role,ignoreCase = true) }
        notifyDataSetChanged()
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence): FilterResults {
                var query:String=p0.toString()
                filteredList=if(query.isEmpty()){
                    userList}
                else{
                    userList.filter { it.name.contains(query, ignoreCase = true)
                            ||it.id.contains(query, ignoreCase = true)||it.role.toString().contains(query, ignoreCase = true) }
                }
                val result=FilterResults()
                result.values=filteredList
                return result
            }

            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList= p1.values as List<User>
                notifyDataSetChanged()
            }

        }

    }

}