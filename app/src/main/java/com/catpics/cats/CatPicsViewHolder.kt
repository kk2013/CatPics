package com.catpics.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catpics.R
import com.catpics.model.Cat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cat_item_row.view.*

class CatPicsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        cat: Cat?
    ) {
        cat?.let {
            Picasso.get()
                .load(cat.url)
                .placeholder(android.R.drawable.ic_menu_camera)
                .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .centerCrop()
                .into(itemView.catImage)
        }
    }

    companion object {

        const val IMAGE_WIDTH = 700
        const val IMAGE_HEIGHT = 1000

        fun create(parent: ViewGroup): CatPicsViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.cat_item_row, parent, false)
            return CatPicsViewHolder(view)
        }
    }
}