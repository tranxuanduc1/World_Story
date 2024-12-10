package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ChipBinding
import com.example.myapplication.databinding.ItemStoryHighScoreLayoutBinding
import com.example.worldstory.duc.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.duc.ducutils.formatFloat
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.model.Story
import okio.Inflater

class Duc_HighScoreStory_Adapter(var context: Context,var dataList:List<Story>): RecyclerView.Adapter<Duc_HighScoreStory_Adapter.ViewModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewModel {
        var inflater= LayoutInflater.from(parent.context)
        var binding= ItemStoryHighScoreLayoutBinding.inflate(inflater)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(
        holder: ViewModel,
        position: Int
    ) {
        var story=dataList[position]
        var binding=holder.binding
        binding.txtTitleStoryItemStoryHighScoreLayout.text=story.title
        binding.txtScoreStoryItemStoryHighScoreLayout.text= formatFloat(story.score)
        binding.txtAuthorStoryItemStoryHighScoreLayout.text=story.author
        binding.imgStoryItemStoryHighScoreLayout.loadImgURL(context,story.imgUrl)
        //nhan vao se di chuyen đen story overview
        binding.btnToStoryOverviewItemStoryHighScoreLayout.setOnClickListener{
            context.toActivity(
                DucStoryOverviewActivity::class.java,
                R.string.key_storyInfo,
                story
            )
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class ViewModel(var binding: ItemStoryHighScoreLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

}