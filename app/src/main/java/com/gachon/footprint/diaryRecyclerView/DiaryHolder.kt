package com.gachon.footprint.diaryRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gachon.footprint.App
import com.gachon.footprint.RecyclerInterface
import com.gachon.footprint.data.ModelFoot
import kotlinx.android.synthetic.main.diary_detail.view.*

class DiaryHolder(itemView: View, recyclerInterface: RecyclerInterface) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val title = itemView.diary_title
    private val contextImg = itemView.diary_img
    private var recyclerInterface: RecyclerInterface? = null

    init {
        itemView.setOnClickListener(this)
        this.recyclerInterface = recyclerInterface
    }

    fun bind(diaries: ModelFoot) {
        title.text = diaries.title
        if (diaries.imageUrl != null) {
            Glide.with(App.instance).load(diaries.imageUrl).override(300, 225).into(contextImg)
        }
    }

    override fun onClick(v: View?) {
        this.recyclerInterface?.onItemClicked(adapterPosition)
    }

}