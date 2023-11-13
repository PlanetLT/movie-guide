package com.planet.movieguide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planet.movieguide.R
import com.planet.movieguide.data.model.Movie
import com.squareup.picasso.Picasso
import com.planet.movieguide.core.constant.Config.Companion.IMG_PREFIX

class MovieAdapter(
    private val favouriteMovieList: List<Movie>,
    private val mList: List<Movie>,
    private val mContext: Context
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    var onItemClick: ((Movie) -> Unit)? = null
    var onFavouriteClick: ((Movie) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = mList[position]
        Picasso.with(mContext).load(IMG_PREFIX + movie.backdropPath).fit().centerCrop()
            .placeholder(R.drawable.movie_place_holder)
            .error(R.drawable.movie_place_holder)
            .into(holder.imgThumbnail)
        if (favouriteMovieList.contains(movie)) {
            holder.imgFavourite.setBackgroundResource(R.drawable.favorite)
        } else {
            holder.imgFavourite.setBackgroundResource(R.drawable.un_favourite)
        }
        holder.tvTitle.text = movie.title
        holder.imgFavourite.setOnClickListener {
            onFavouriteClick?.invoke(mList[position])
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mList[position])
        }

    }


    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imgThumbnail: ImageView = itemView.findViewById(R.id.img_thumbnail)
        val imgFavourite: ImageView = itemView.findViewById(R.id.img_favourite)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

    }
}