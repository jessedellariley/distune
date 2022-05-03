package com.example.distune.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.distune.Favorite
import com.example.distune.Playlist
import com.example.distune.R
import com.parse.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DiscoverFragment : Fragment() {

    var usersToDiscover = mutableListOf<JSONObject>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUsersToDiscover()
        var i = 0
        if (usersToDiscover.size != 0) {
            // load first user

            // On swipe right

            // On swipe left
        }
    }

    private fun getUsersToDiscover() {
        val query: ParseQuery<ParseUser> = ParseQuery.getQuery("User")
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().username)
        query.whereEqualTo("public",true)
        query.findInBackground(object : FindCallback<ParseUser> {
            override fun done(results: MutableList<ParseUser>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e("DiscoverFragment", "Error loading users")
                    e.printStackTrace()
                } else {
                    if (results != null && results.size > 0) {
                        for (result in results) {
                            var json = JSONObject()
                            json.put("username", result.username)
                            json.put("image", result.getString("image"))
                            json.put("bio", result.getString("bio"))
                            val query: ParseQuery<Favorite> = ParseQuery.getQuery(Favorite::class.java)
                            query.whereEqualTo("user",result)
                            query.findInBackground(object : FindCallback<Favorite> {
                                override fun done(favoriteResults: MutableList<Favorite>?, e: ParseException?) {
                                    if (e != null) {
                                        // Something has gone wrong
                                        Log.e("DiscoverFragment", "Error loading user's favorites")
                                        e.printStackTrace()
                                    } else {
                                        if (favoriteResults != null && favoriteResults.size > 0) {
                                            var favs = JSONArray()
                                            for (fav in favoriteResults) {
                                                var favJsonObj = JSONObject()
                                                favJsonObj.put("spotifyId",fav.getString("spotifyId"))
                                                favJsonObj.put("title",fav.getString("title"))
                                                favJsonObj.put("image",fav.getString("image"))
                                                favJsonObj.put("artist",fav.getString("artist"))
                                                favJsonObj.put("index",fav.getNumber("index"))
                                                favs.put(favJsonObj)
                                            }
                                            json.put("favorites",favs)
                                            usersToDiscover.add(json)
                                        } else {
                                            Log.d("DiscoverFragment", "No favorites found for this user")
                                        }
                                    }
                                }
                            })
                        }
                    } else {
                        Log.d("DiscoverFragment", "No users found")
                    }
                }
            }
        })
    }
}