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

class FollowerFragment : Fragment() {

    lateinit var followersRecyclerView: RecyclerView
    lateinit var adapter: FollowerAdapter
    var allFollowers: MutableList<Follower> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersRecyclerView = view.findViewById(R.id.followersRecyclerView)

        adapter = FollowerAdapter(requireContext(), allFollowers)
        followersRecyclerView.adapter = adapter

        followersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        getFollowers()
    }

    private fun getFollowers() {
        val query: ParseQuery<Follower> = ParseQuery.getQuery(Follower::class.java)
        query.whereEqualTo(Follower.KEY_USER,ParseUser.getCurrentUser())
        query.findInBackground(object : FindCallback<Follower> {
            override fun done(results: MutableList<Follower>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e("PlaylistsFragment", "Error fetching playlists")
                    e.printStackTrace()
                } else {
                    if (results != null && results.size > 0) {
                        for (result in results) {
                            allFollowers.add(result)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}