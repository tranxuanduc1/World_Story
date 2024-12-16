package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.view.admin_dialog.ChangeRole
import com.example.worldstory.view.admin_dialog.ChangeUserAvtDialog
import com.example.worldstory.view.admin_dialog.ChangeUserInforDialog
import com.example.worldstory.view.admin_dialog.ChangeUserPasswordDialog
import com.example.worldstory.data.model.User
import com.example.worldstory.dat.admin_viewholder.UserViewHolder
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserAdapter(
    private val userList: MutableList<User>,
    private var color: Int,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<UserViewHolder>(), Filterable {
    private var filteredList: List<User> = userList

    override fun getItemCount(): Int = filteredList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_row, parent, false)
        return UserViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredList[position]


        Picasso.get().load(user.imgAvatar).into(holder.avt_user_col)
        holder.column2.text = user.userID.toString()
        holder.column1.text = "Nickname: ${user.nickName}"
        holder.column3.text = getFormatedDate(normalizeDateTime(user.createdDate))
        holder.column4.text = "Username: ${user.userName}"
        holder.itemView.setOnLongClickListener {
            showPopupMenu(holder.itemView, user)
            true
        }
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
                val query: String = p0.toString().trim()
                filteredList = if (query.isEmpty()) {
                    userList
                } else {
                    userList.filter {
                        it.nickName.contains(query, ignoreCase = true)
                                || it.userID.toString().contains(query, ignoreCase = true)
                                || it.userName.contains(query, ignoreCase = true)
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
        userList.clear()
        userList.addAll(users)
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


    // Hàm hiển thị PopupMenu
    private fun showPopupMenu(view: View, item: User) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.change_password -> {
                    // Xử lý đổi mật khẩu
                    val dialog = ChangeUserPasswordDialog.newInstance(item.userID)
                    dialog.show(fragmentManager, "ChangePassword")
                }

                R.id.change_info -> {
                    // Xử lý đổi thông tin
                    val dialog = ChangeUserInforDialog.newInstance(item.userID)
                    dialog.show(fragmentManager, "ChangeInfor")
                }

                R.id.change_avatar -> {
                    // Xử lý đổi avatar
                    val dialog = ChangeUserAvtDialog.newInstance(item.userID)
                    dialog.show(fragmentManager, "ChangeAvatar")
                }

                R.id.change_role -> {
                    val dialog = ChangeRole.newInstance(item.userID)
                    dialog.show(fragmentManager, "ChangeRole")
                }
            }
            true
        }
        popupMenu.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormatedDate(dateTime: String?): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val date = LocalDateTime.parse(dateTime, inputFormatter)
        val formattedDate = date.format(outputFormatter)
        return formattedDate

    }

    fun normalizeDateTime(input: String): String {
        return input.replace(Regex("\\b(\\d)\\b"), "0$1") // Thêm số 0 vào giờ 1 chữ số
    }
}