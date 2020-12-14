package com.mindorks.framework.mvvm.ui.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindorks.framework.mvvm.BuildConfig
import com.mindorks.framework.mvvm.R
import com.mindorks.framework.mvvm.data.model.PhotoItem
import com.mindorks.framework.mvvm.ui.main.view.ContentActivity
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(private val photos: ArrayList<PhotoItem>, private val context: Context) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    val favorites: HashSet<Int> = hashSetOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: PhotoItem) {
            itemView.text_view.text = photo.name

            Glide.with(context)
                .load(photo.image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(itemView.image_view)

            val favorite = favorites.contains(photo.id)
            itemView.favorite_icon.setImageResource(icon(favorite))

            itemView.setOnClickListener {
                val intent = Intent(context, ContentActivity::class.java).apply {
                    putExtra("image", photo.image)
                }
                context.startActivity(intent)
            }

            itemView.favorite_icon.setOnClickListener {
                val contain = favorites.contains(photo.id)
                if (contain) favorites.remove(photo.id) else favorites.add(photo.id)
                itemView.favorite_icon.setImageResource(icon(!contain))
                notifyDataSetChanged()
            }
        }

        private fun icon(favorite: Boolean) =
            if (favorite) R.drawable.ic_favorite else R.drawable.ic_no_favorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(photos[position])

    fun update(favorites: HashSet<Int>, list: ArrayList<PhotoItem>) {
        if (photos.size > BuildConfig.MAX_HEAP)
            photos.clear()

        this.favorites.addAll(favorites)
        this.photos.addAll(list)
        notifyDataSetChanged()
    }
}