package com.example.distune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

var users = JSONArray()

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(1000) {
            getUsersToDiscover()
        }
        Timer().schedule(2000) {
            var i = Intent(applicationContext,MainActivity::class.java)
            i.putExtra("USERS_TO_DISCOVER",users.toString())
            startActivity(i)
        }
    }

    private fun getUsersToDiscover() {
        val query: ParseQuery<ParseUser> = ParseUser.getQuery()
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().username)
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
                            json.put("user",result)
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
                                            Log.d("DiscoverFragment","Retrieved user's favorites")
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
                                            users.put(json)
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