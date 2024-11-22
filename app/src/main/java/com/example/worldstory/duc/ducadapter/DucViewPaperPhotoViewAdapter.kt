package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.worldstory.dat.admin_adapter.ViewPagerAdapter
import com.example.worldstory.model.Image
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutDucPhotoviewBinding
import com.example.worldstory.duc.ducutils.loadImgURL

import com.github.chrisbanes.photoview.PhotoView

class DucViewPaperPhotoViewAdapter(var context:Context, private var listOfImages: List<Image>) :
    RecyclerView.Adapter<DucViewPaperPhotoViewAdapter.Paper2ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DucViewPaperPhotoViewAdapter.Paper2ViewHolder {

       var view= LayoutInflater.from(parent.context).inflate(R.layout.layout_duc_photoview,parent,false)
       return Paper2ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DucViewPaperPhotoViewAdapter.Paper2ViewHolder,
        position: Int
    ) {
        holder.photoView.loadImgURL(context,listOfImages[position].imgFilePath)
    }

    override fun getItemCount(): Int {
       return listOfImages.size
    }

    class Paper2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoView=itemView.findViewById<PhotoView>(R.id.photoView_image_layoutDucPhotoView)

    }
}