package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemUserPostStoryLayoutBinding
import com.example.worldstory.view.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.duc.ducutils.dateFromDateTime
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.data.model.User
import com.example.worldstory.data.model.Story

class Duc_StoryPostedByUser_Adapter(
    var context: Context,
    var storiesData: List<Story>,
    var user: User,
) : RecyclerView.Adapter<Duc_StoryPostedByUser_Adapter.Viewhoder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewhoder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = ItemUserPostStoryLayoutBinding.inflate(inflater)
        return Viewhoder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewhoder,
        position: Int
    ) {
        var binding = holder.binding
        var story = storiesData[position]
        binding.txtNickNameUserUserPostStory.text = user.nickName
        // so luong truyen ma user da dang tai
        binding.txtDatePostUserPostStory.text = story.createdDate.dateFromDateTime()
        binding.imgAvatarUserUserPostStory.loadImgURL(context, user.imgAvatar)
        binding.imgImgStoryUserPostStory.loadImgURL(context, story.imgUrl)
        binding.imgImgStoryUserPostStory.setOnClickListener {
            context.toActivity(
                DucStoryOverviewActivity::class.java,
                getKeyStoryInfo(context),
                story
            )
        }
    }

    override fun getItemCount(): Int {
        return storiesData.size
    }

    inner class Viewhoder(var binding: ItemUserPostStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}