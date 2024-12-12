package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.CardStoryItemLayoutBinding
import com.example.myapplication.databinding.ItemUserCreatedStoryLayoutBinding
import com.example.myapplication.databinding.ListCardStoriesLayoutBinding
import com.example.worldstory.duc.ducactivity.DucInfoUserActivity
import com.example.worldstory.duc.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.duc.ducdataclass.DucStoriesAndGenreDataClass
import com.example.worldstory.duc.ducutils.changeShapeBackgroundColorByScore
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.formatFloat
import com.example.worldstory.duc.ducutils.getKeyUserInfo
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducutils.toActivityStoriesByGenre
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Story
import com.example.worldstory.model.User

class Duc_CardStoryAndGenre_Adapter(
    var context: Context,
    var dataStoriesAndGenre: List<DucStoriesAndGenreDataClass>,
    var isText: Boolean,
    var numStories: Int,
) : RecyclerView.Adapter<Duc_CardStoryAndGenre_Adapter.Viewhoder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewhoder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = ListCardStoriesLayoutBinding.inflate(inflater)
        return Viewhoder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewhoder,
        position: Int
    ) {
        var binding = holder.binding
        var storiesAndGenre = dataStoriesAndGenre[position]
        binding.genreListCardStory.text = storiesAndGenre.genre.genreName

        //lay so luon stories can thiet
        storiesAndGenre.listOfStories =
            if (storiesAndGenre.listOfStories.size >= numStories) storiesAndGenre.listOfStories.take(
                numStories
            ) else storiesAndGenre.listOfStories
        storiesAndGenre.listOfStories.forEach { story->
            var bindingCardView = CardStoryItemLayoutBinding.inflate(LayoutInflater.from(context))
            bindingCardView.txtTitleCardStoryItemLayout.text=story.title
            bindingCardView.txtAuthorCardStoryItemLayout.text=story.author
            bindingCardView.imgCardStoryItemLayout.loadImgURL(context,story.imgUrl)
            bindingCardView.txtRankCardStoryItemLayout.text= formatFloat( story.score)
            bindingCardView.idStoryCardStoryItem.text=story.storyID.toString()
            bindingCardView.constraintLayoutCardStoryLayout.changeShapeBackgroundColorByScore(story.score)
            bindingCardView.root.setOnClickListener{
                // truyen mot dataclass den activity moi
                context.toActivity(
                    DucStoryOverviewActivity::class.java,
                    context.getString(R.string.key_storyInfo),
                    story
                )
            }
            binding.root.apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // layout_columnWeight="1"
                    setGravity(Gravity.CENTER)
                    setMargins(0, 0, 0, 10.dpToPx())

                }
            }
            binding.gridLayoutListCardStory.addView(bindingCardView.root)

        }

        //khi nhan vao xem them , hien thi cac truyen co cung genre
        binding.txtSeeMoreListCardStory.setOnClickListener {
            context.toActivityStoriesByGenre(isText, storiesAndGenre.genre)
        }
    }

    override fun getItemCount(): Int {
        return dataStoriesAndGenre.size
    }

    inner class Viewhoder(var binding: ListCardStoriesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}