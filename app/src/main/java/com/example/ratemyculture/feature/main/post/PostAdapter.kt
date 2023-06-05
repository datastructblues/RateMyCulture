package com.example.ratemyculture.feature.main.post

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ratemyculture.R
import com.example.ratemyculture.data.model.sharings.Sharing
import com.squareup.picasso.Picasso

class PostAdapter(
    private var sharingList: List<Sharing>,
    private var user_name:String?,
    private var profile_picture:String?
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val sharing = sharingList[position]
        holder.bind(sharing)
    }

    override fun getItemCount(): Int {
        return sharingList.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.selectedPostImage)
        private val caption = itemView.findViewById<TextView>(R.id.postCaption)
        private val userName = itemView.findViewById<TextView>(R.id.postUsername)
        private val postDate = itemView.findViewById<TextView>(R.id.postDate)
        private val profilePicture = itemView.findViewById<ImageView>(R.id.post_profile_image)
        fun bind(item: Sharing) {
            Picasso.get().load(item.photoUrl).into(image)
            caption.text = item.caption
            userName.text = user_name
            Picasso.get().load(profile_picture).into(profilePicture)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateList(newList: List<Sharing>) {
            sharingList = newList
            notifyDataSetChanged()
        }
    }
}
