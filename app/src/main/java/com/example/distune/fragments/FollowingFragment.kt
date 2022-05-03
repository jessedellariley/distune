package com.example.distune.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.distune.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FollowingFragment : Fragment() {

    lateinit var followingRecyclerView: RecyclerView
    lateinit var adapter: FollowingAdapter
    var allFollowing: MutableList<Follower> = mutableListOf()
    lateinit var userForProfile : ParseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var username = arguments?.getString("WHICH_USER")
        val query: ParseQuery<ParseUser> = ParseUser.getQuery()
        query.whereEqualTo("username", username)
        var results: MutableList<ParseUser>? = query.find()
        userForProfile = results!![0]
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingRecyclerView = view.findViewById(R.id.followingRecyclerView)

        adapter = FollowingAdapter(requireContext(), allFollowing)
        followingRecyclerView.adapter = adapter

        followingRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        getFollowing()
    }

    private fun getFollowing() {
        val query: ParseQuery<Follower> = ParseQuery.getQuery(Follower::class.java)
        query.whereEqualTo(Follower.KEY_FOLLOWER, userForProfile)
        query.findInBackground(object : FindCallback<Follower> {
            override fun done(results: MutableList<Follower>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e("FollowingFragment", "Error fetching following")
                    e.printStackTrace()
                } else {
                    if (results != null && results.size > 0) {
                        for (result in results) {
                            allFollowing.add(result)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}