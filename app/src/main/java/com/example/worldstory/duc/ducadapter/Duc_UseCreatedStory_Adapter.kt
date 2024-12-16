package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemUserCreatedStoryLayoutBinding
import com.example.worldstory.view.ducactivity.DucInfoUserActivity
import com.example.worldstory.duc.ducutils.getKeyUserInfo
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.data.model.User

class Duc_UseCreatedStory_Adapter(
    var context: Context,
    var usersAndNumlist: List<Pair<User, Int>>,
    var isText: Boolean
): RecyclerView.Adapter<Duc_UseCreatedStory_Adapter.Viewhoder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewhoder {
        var inflater= LayoutInflater.from(parent.context)
        var binding= ItemUserCreatedStoryLayoutBinding.inflate(inflater)
        return Viewhoder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewhoder,
        position: Int
    ) {
        var user=usersAndNumlist[position].first
        var numCreated=usersAndNumlist[position].second
        var binding=holder.binding

        binding.txtNicknameItemUserCreatedStory.text=user.nickName
        // so luong truyen ma user da dang tai
        binding.txtNumStoriesItemUserCreatedStory.text=numCreated.toString()
        binding.imgAvatarItemUserCreatedStory.loadImgURL(context,user.imgAvatar)
        binding.btnToInfoUserItemUserCreatedStory.setOnClickListener{
            context.toActivity(DucInfoUserActivity::class.java, getKeyUserInfo(context),user)
        }
    }

    override fun getItemCount(): Int {
        return usersAndNumlist.size
    }

    inner class Viewhoder(var binding: ItemUserCreatedStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}