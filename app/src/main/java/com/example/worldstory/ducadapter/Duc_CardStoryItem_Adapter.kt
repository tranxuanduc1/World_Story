package com.example.worldstory.ducadapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.worldstory.ducdataclass.DucStoryDataClass
import com.example.myapplication.R
import com.example.worldstory.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.ducutils.changeShapeBackgroundColorByScore
import com.example.worldstory.ducutils.dpToPx
import com.example.worldstory.ducutils.toActivity


class Duc_CardStoryItem_Adapter(var context: Context, private val dataList: ArrayList<DucStoryDataClass>)
    : RecyclerView.Adapter<Duc_CardStoryItem_Adapter.ViewHolderClass>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        var itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_story_item_layout,
            parent,false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolderClass,
        position: Int
    ) {
        var currentItem = dataList[position]
        holder.itemView.apply {
            layoutParams= GridLayout.LayoutParams().apply {
                setGravity(Gravity.CENTER)
                columnSpec= GridLayout.spec(GridLayout.UNDEFINED,1f)
                setMargins(5.dpToPx(),5.dpToPx(),5.dpToPx(),10.dpToPx())
            }
        }

        holder.title.text=currentItem.title
        holder.author.text=currentItem.author
        holder.imgURL.setImageResource(currentItem.imgURL)

        holder.score.text= (currentItem.score).toString()
        holder.constraintLayout.changeShapeBackgroundColorByScore(currentItem.score)
//        holder.constraintLayout.setBackgroundResource(R.drawable.shape_yellow_card_story_item_layout)
//        setColorScore(currentItem.score,holder)
        holder.idStory.text=currentItem.idStory.toString()
        holder.itemView.setOnClickListener{


            // truyen mot dataclass den activity moi
            context.toActivity(DucStoryOverviewActivity::class.java, R.string.key_storyInfo,currentItem)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView : View): RecyclerView.ViewHolder(itemView){
        var title=itemView.findViewById<TextView>(R.id.txtTitleCardStoryItemLayout)
        var author =itemView.findViewById<TextView>(R.id.txtAuthorCardStoryItemLayout)
        var imgURL=itemView.findViewById<ImageView>(R.id.imgCardStoryItemLayout)
        var score =itemView.findViewById<TextView>(R.id.txtRankCardStoryItemLayout)
        var constraintLayout=itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutCardStoryLayout)
        var idStory=itemView.findViewById<TextView>(R.id.idStory_CardStoryItem)
    }
    fun setColorScore(score: Float,holder: Duc_CardStoryItem_Adapter.ViewHolderClass){
        if(score>=4f){
            holder.constraintLayout.setBackgroundResource(R.drawable.shape_green_story_item_layout)

        }else if(score>=2.5f && score<4f ){
            holder.constraintLayout.setBackgroundResource(R.drawable.shape_yellow_card_story_item_layout)

        }else{
            holder.constraintLayout.setBackgroundResource(R.drawable.shape_red_card_story_item_layout)

        }
    }
}
