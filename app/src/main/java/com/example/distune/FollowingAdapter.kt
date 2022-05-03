package com.example.distune

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parse.ParseException
import com.parse.ParseUser

class FollowingAdapter(val context: Context, private val following: List<Follower>) : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_follower,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val following = following[position]
        holder.bind(following)
    }

    override fun getItemCount(): Int {
        return following.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val followingUsername: TextView = itemView.findViewById(R.id.follower)
        private val profileImage: ImageView = itemView.findViewById(R.id.profileImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(follower: Follower) {
            try {
                followingUsername.text = follower.getUser()?.fetchIfNeeded()?.username
            } catch (e : ParseException) {
                Log.d("FollowingAdapter", e.toString())
                e.printStackTrace()
            }
            //Glide.with(itemView.context).load(follower.getUser()?.getString("image")).into(profileImage)
        }

        override fun onClick(p0: View?) {
            val following = following[adapterPosition].getUser()?.username
            val intent = Intent(context, FollowProfileActivity::class.java)
            intent.putExtra(FOLLOWER_EXTRA,following)
            context.startActivity(intent)
        }
    }
}