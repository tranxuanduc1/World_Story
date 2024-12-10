package com.example.worldstory.duc.ducutils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SetItemDecorationForRecyclerView (var spaceT: Int,var spaceB: Int,var spaceR: Int,var spaceL: Int): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left=spaceL.dpToPx()
        outRect.right=spaceR.dpToPx()
        outRect.bottom=spaceB.dpToPx()

//        if(parent.getChildItemId(view)<1){
//            outRect.top=spaceT.dpToPx()
//
//        }
    }
}