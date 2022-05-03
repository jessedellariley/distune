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

const val FOLLOWER_EXTRA = "FOLLOWER_EXTRA"
class FollowerAdapter(val context: Context, private val followers: List<Follower>) : RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_follower,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = followers[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int {
        return followers.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val followerUsername: TextView = itemView.findViewById(R.id.follower)
        private val profileImage: ImageView = itemView.findViewById(R.id.profileImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(follower: Follower) {
            try {
                followerUsername.text = follower.getFollower()?.fetchIfNeeded()?.username
            } catch (e : ParseException) {
                Log.d("FollowerAdapter", e.toString())
                e.printStackTrace()
            }
            //Glide.with(itemView.context).load(follower.getUser()?.getString("image")).into(profileImage)
        }

        override fun onClick(p0: View?) {
            val follower = followers[adapterPosition].getFollower()?.username
            val intent = Intent(context, FollowProfileActivity::class.java)
            intent.putExtra(FOLLOWER_EXTRA,follower)
            context.startActivity(intent)
        }
    }
}