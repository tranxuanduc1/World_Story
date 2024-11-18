package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.duc.ducdataclass.DucGenreDataClass
import com.example.worldstory.duc.ducutils.toActivityStoriesByGenre

class Duc_Button_Adapter(
    var context: Context,
    private val dataList: ArrayList<DucGenreDataClass>,
    private var isComic: Boolean = true
) :
    RecyclerView.Adapter<Duc_Button_Adapter.ViewHolderClass>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        var itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.genre_item_layout, parent, false
        )

        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolderClass,
        position: Int
    ) {
        holder.btnGenre.setText(dataList[position].title)
        holder.txtIDGenre.setText(dataList[position].idGenre.toString())
        holder.btnGenre.setOnClickListener {

            context.toActivityStoriesByGenre(isComic,dataList[position])

//            var keyIsComic = context.resources.getString(R.string.key_isComic)
//            var keyGenreInfo = context.resources.getString(R.string.key_genreInfo)
//            var bundle = Bundle()
//            bundle.putBoolean(keyIsComic, isComic)
//            bundle.putParcelable(keyGenreInfo, dataList[position])
//            context.toActivity(
//                StoriesByGenreActivity::class.java,
//                R.string.key_storiesByGenre,
//                bundle
//            )
//            var intent= Intent(context, StoriesByGenreActivity::class.java)
//
//            intent.putExtra(context.resources.getString( R.string.key_storyInfo),currentItem)
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btnGenre = itemView.findViewById<Button>(R.id.btn_genreItemlayout)
        var txtIDGenre = itemView.findViewById<TextView>(R.id.txtIDGenre_genreItemlayout)
    }
}