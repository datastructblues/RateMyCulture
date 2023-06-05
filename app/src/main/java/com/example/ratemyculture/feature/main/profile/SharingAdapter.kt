package com.example.ratemyculture.feature.main.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ratemyculture.R
import com.example.ratemyculture.data.model.sharings.Sharing
import com.squareup.picasso.Picasso

class SharingAdapter(
    private var sharingList: List<Sharing>,
    private val onItemClick: (Sharing) -> Unit
) : RecyclerView.Adapter<SharingAdapter.SharingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return SharingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SharingViewHolder, position: Int) {
        val sharing = sharingList[position]
        holder.bind(sharing)
    }

    override fun getItemCount(): Int {
        return sharingList.size
    }

    inner class SharingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.photo_item_image)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val sharing = sharingList[position]
                    onItemClick(sharing)
                }
            }
        }

        fun bind(item: Sharing) {
            Picasso.get().load(item.photoUrl).into(image)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Sharing>) {
        sharingList = emptyList()
        sharingList = newList
        notifyDataSetChanged()
    }
}
