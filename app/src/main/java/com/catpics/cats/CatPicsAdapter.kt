package com.catpics.cats

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.catpics.model.Cat

class CatsPicsAdapter() :
    PagedListAdapter<Cat, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CatPicsViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CatPicsViewHolder.create(parent)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cat>() {

            override fun areItemsTheSame(oldItem: Cat, newItem: Cat) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cat, newItem: Cat) = oldItem == newItem
        }
    }
}