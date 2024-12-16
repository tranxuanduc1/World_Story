package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemChaperMarkedLayoutBinding
import com.example.worldstory.view.ducactivity.DucChapterActivity
import com.example.worldstory.data.ducdataclass.DucComboChapterDataClass
import com.example.worldstory.duc.ducutils.getKeyChapterInfo
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.getKeyMainChapter
import com.example.worldstory.duc.ducutils.getKeyNextChapter
import com.example.worldstory.duc.ducutils.getKeyPreviousChapter
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.data.model.Chapter
import com.example.worldstory.data.model.Story

class Duc_ChapterMarked_Adapter(
    var context: Context,
    var dataList: List<DucComboChapterDataClass>
) :
    RecyclerView.Adapter<Duc_ChapterMarked_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = ItemChaperMarkedLayoutBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        var mainChapter = dataList[position].mainChapter
        var preChapter=dataList[position].preChapter
        var nextChapter=dataList[position].nextChapter
        var story=dataList[position].story
        var binding = holder.binding
        binding.txtTitleChapterItemChapterMarked.text =mainChapter.title
        binding.txtTitleStoryItemChapterMarked.text = story.title
        binding.imgStoryItemChapterMarked.loadImgURL(context,story.imgUrl)
        binding.btnToChapterChapterMark.setOnClickListener {
            var bundle = makeBundleToChapterActivity(preChapter,mainChapter,nextChapter,story)
            context.toActivity(DucChapterActivity::class.java, getKeyChapterInfo(context),bundle)
        }

    }

    private fun makeBundleToChapterActivity(
        preChapter: Chapter?,
        mainChapter: Chapter,
        nextChapter: Chapter?,
        story: Story
    ) : Bundle{
        var bundle = Bundle()
        bundle.putParcelable(getKeyNextChapter(context), nextChapter)
        bundle.putParcelable(getKeyMainChapter(context), mainChapter)
        bundle.putParcelable(getKeyPreviousChapter(context), preChapter)
        bundle.putParcelable(getKeyStoryInfo(context), story)
        return bundle
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(var binding: ItemChaperMarkedLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}