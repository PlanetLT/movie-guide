package com.planet.movieguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planet.movieguide.R

class PageNumberAdapter(private val nList: List<Int>) :
    RecyclerView.Adapter<PageNumberAdapter.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.number_item_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val number = nList[position]

        holder.textView.text = number.toString()

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(nList[position])
        }

    }


    override fun getItemCount(): Int {
        return nList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_number)
    }
}