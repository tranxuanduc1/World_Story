package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemStoryHighScoreLayoutBinding
import com.example.worldstory.view.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.data.ducdataclass.DucComboHighScoreStoryDataClass
import com.example.worldstory.duc.ducutils.formatFloat
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity

class Duc_HighScoreStory_Adapter(var context: Context, var dataList: List<DucComboHighScoreStoryDataClass>) :
    RecyclerView.Adapter<Duc_HighScoreStory_Adapter.ViewModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewModel {
        var inflater = LayoutInflater.from(parent.context)
        var binding = ItemStoryHighScoreLayoutBinding.inflate(inflater)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(
        holder: ViewModel,
        position: Int
    ) {
        var combo = dataList[position]
        var binding = holder.binding
        binding.txtTitleStoryItemStoryHighScoreLayout.text = combo.story.title
        binding.txtScoreStoryItemStoryHighScoreLayout.text = formatFloat(combo.story.score)
        binding.txtAuthorStoryItemStoryHighScoreLayout.text = combo.story.author
        binding.imgStoryItemStoryHighScoreLayout.loadImgURL(context, combo.story.imgUrl)
        binding.txtNumRatingStoryItemStoryHighScoreLayout.text="${combo.numRating} Đánh giá"
        binding.txtNumCommentStoryItemStoryHighScoreLayout.text="${combo.numComment} Bình luận"
        //nhan vao se di chuyen đen story overview
        binding.btnToStoryOverviewItemStoryHighScoreLayout.setOnClickListener {
            context.toActivity(
                DucStoryOverviewActivity::class.java,
                R.string.key_storyInfo,
                combo.story
            )
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class ViewModel(var binding: ItemStoryHighScoreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}