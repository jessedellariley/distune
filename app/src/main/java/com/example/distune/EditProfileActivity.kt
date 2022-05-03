package com.example.distune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.parse.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        var isPublic = findViewById<Switch>(R.id.publicProfile)
        var username = findViewById<TextView>(R.id.tv_username)
        var bio = findViewById<EditText>(R.id.etBio)
        var albumFavorite1 = findViewById<ImageView>(R.id.favorite1AlbumCover)
        var titleFavorite1 = findViewById<TextView>(R.id.favorite1SongTitle)
        var artistFavorite1 = findViewById<TextView>(R.id.favorite1Artist)
        var editFavorite1 = findViewById<ImageView>(R.id.ivEditFavorite1)
        var albumFavorite2 = findViewById<ImageView>(R.id.favorite2AlbumCover)
        var titleFavorite2 = findViewById<TextView>(R.id.favorite2SongTitle)
        var artistFavorite2 = findViewById<TextView>(R.id.favorite2Artist)
        var editFavorite2 = findViewById<ImageView>(R.id.ivEditFavorite2)
        var albumFavorite3 = findViewById<ImageView>(R.id.favorite3AlbumCover)
        var titleFavorite3 = findViewById<TextView>(R.id.favorite3SongTitle)
        var artistFavorite3 = findViewById<TextView>(R.id.favorite3Artist)
        var editFavorite3 = findViewById<ImageView>(R.id.ivEditFavorite3)
        var btnSave = findViewById<Button>(R.id.btnSave)

        var user = ParseUser.getCurrentUser()

        username.text = user.username
        bio.setText(user.getString("bio"))

        if (user.getBoolean("public")) {
            isPublic.isChecked = true
            // Load user top songs from Parse backend
            val query: ParseQuery<Favorite> = ParseQuery.getQuery(Favorite::class.java)
            query.whereEqualTo(Favorite.KEY_USER, user)
            query.findInBackground(object : FindCallback<Favorite> {
                override fun done(results: MutableList<Favorite>?, e: ParseException?) {
                    if (e != null) {
                        // Something has gone wrong
                        Log.e("EditProfileActivity", "Error fetching song")
                        e.printStackTrace()
                    } else {
                        if (results != null && results.size > 0) {
                            for (result in results) {
                                if (result.getNumber("index") == 0) {
                                    Glide.with(applicationContext).load(result.getString("image")).into(albumFavorite1)
                                    titleFavorite1.text = result.getString("title")
                                    artistFavorite1.text = result.getString("artist")
                                } else if (result.getNumber("index") == 1) {
                                    Glide.with(applicationContext).load(result.getString("image")).into(albumFavorite2)
                                    titleFavorite2.text = result.getString("title")
                                    artistFavorite2.text = result.getString("artist")
                                } else if (result.getNumber("index") == 2) {
                                    Glide.with(applicationContext).load(result.getString("image")).into(albumFavorite3)
                                    titleFavorite3.text = result.getString("title")
                                    artistFavorite3.text = result.getString("artist")
                                }
                            }
                        }
                    }
                }
            })
            // Change relevant UI elements to visible
            findViewById<ConstraintLayout>(R.id.favorite1).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.favorite2).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.favorite3).visibility = View.VISIBLE
        } else {
            isPublic.isChecked = false
        }

        isPublic.setOnCheckedChangeListener { _, _ ->
            if (isPublic.isChecked) {
                // Load user top songs from Spotify into Parse
                getUserTopTracks()
                // Load user top songs from Parse backend into UI
                val query: ParseQuery<Favorite> = ParseQuery.getQuery(Favorite::class.java)
                query.whereEqualTo(Favorite.KEY_USER, user)
                query.findInBackground(object : FindCallback<Favorite> {
                    override fun done(results: MutableList<Favorite>?, e: ParseException?) {
                        if (e != null) {
                            // Something has gone wrong
                            Log.e("EditProfileActivity", "Error fetching song")
                            e.printStackTrace()
                        } else {
                            if (results != null && results.size > 0) {
                                for (result in results) {
                                    if (result.getNumber("index") == 0) {
                                        Glide.with(applicationContext).load(result.getString("image")).into(albumFavorite1)
                                        titleFavorite1.text = result.getString("title")
                                        artistFavorite1.text = result.getString("artist")
                                    } else if (result.getNumber("index") == 1) {
                                        Glide.with(applicationContext).load(result.getString("image")).into(albumFavorite2)
                                        titleFavorite2.text = result.getString("title")
                                        artistFavorite2.text = result.getString("artist")
                                    } else if (result.getNumber("index") == 2) {
                                        Glide.with(applicationContext).load(result.getString("image")).into(albumFavorite3)
                                        titleFavorite3.text = result.getString("title")
                                        artistFavorite3.text = result.getString("artist")
                                    }
                                }
                            }
                        }
                    }
                })
                // Change relevant UI elements to visible
                findViewById<ConstraintLayout>(R.id.favorite1).visibility = View.VISIBLE
                findViewById<ConstraintLayout>(R.id.favorite2).visibility = View.VISIBLE
                findViewById<ConstraintLayout>(R.id.favorite3).visibility = View.VISIBLE
            } else {
                // Change relevant UI elements to invisible
                findViewById<ConstraintLayout>(R.id.favorite1).visibility = View.INVISIBLE
                findViewById<ConstraintLayout>(R.id.favorite2).visibility = View.INVISIBLE
                findViewById<ConstraintLayout>(R.id.favorite3).visibility = View.INVISIBLE
            }
        }

        editFavorite1.setOnClickListener() {

        }

        editFavorite2.setOnClickListener() {

        }

        editFavorite3.setOnClickListener() {

        }

        btnSave.setOnClickListener() {
            var newBio = bio.text?.toString()
            if (newBio != null) {
                user.put("bio",newBio)
            } else {
                user.put("bio","Bio")
            }
            user.put("public",isPublic.isChecked)
            user.saveInBackground {
                if (it != null) {
                    it.localizedMessage?.let { message ->
                        Log.e(
                            "EditProfileActivity",
                            message
                        )
                    }
                }
            }
            var i = Intent(this, MainActivity::class.java)
            i.putExtra("FRAGMENT_TO_LOAD","profile")
            startActivity(i)
        }
    }

    private fun getUserTopTracks() {
        var url = "https://api.spotify.com/v1/me/top/tracks"
        var token = ParseUser.getCurrentUser().getString("token")
        var client = OkHttpClient()
        var httpBuilder : HttpUrl.Builder = HttpUrl.parse(url)!!.newBuilder()
        httpBuilder.addQueryParameter("limit","3")
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(httpBuilder.build())
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Log.d("EditProfileActivity","Retreived top tracks")
                    val responseBody = JSONObject(response.body()!!.string())
                    val responseItems = responseBody.getJSONArray("items")
                    for (i in 0 until responseItems.length()) {
                        val trackInfo = responseItems.getJSONObject(i)
                        val query: ParseQuery<Favorite> = ParseQuery.getQuery(Favorite::class.java)
                        query.whereEqualTo(Favorite.KEY_USER,ParseUser.getCurrentUser())
                        query.whereEqualTo(Favorite.KEY_INDEX,i)
                        query.findInBackground(object : FindCallback<Favorite> {
                            override fun done(results: MutableList<Favorite>?, e: ParseException?) {
                                if (e != null) {
                                    // Something has gone wrong
                                    Log.e("EditProfileAcivity", "Error fetching playlists")
                                    e.printStackTrace()
                                } else {
                                    if (results != null && results.size > 0) {
                                        for (result in results) {
                                            result.setId(trackInfo.getString("id"))
                                            result.setTitle(trackInfo.getString("name"))
                                            result.setImage(trackInfo.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"))
                                            result.setArtist(trackInfo.getJSONArray("artists").getJSONObject(0).getString("name"))
                                            result.setUser(ParseUser.getCurrentUser())
                                            result.setIndex(i)

                                            result.saveInBackground {
                                                if (it != null) {
                                                    it.localizedMessage?.let { message ->
                                                        Log.e(
                                                            "EditProfileActivity",
                                                            message
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        var track = Favorite()

                                        track.setId(trackInfo.getString("id"))
                                        track.setTitle(trackInfo.getString("name"))
                                        track.setImage(trackInfo.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"))
                                        track.setArtist(trackInfo.getJSONArray("artists").getJSONObject(0).getString("name"))
                                        track.setUser(ParseUser.getCurrentUser())
                                        track.setIndex(i)

                                        track.saveInBackground {
                                            if (it != null) {
                                                it.localizedMessage?.let { message ->
                                                    Log.e(
                                                        "EditProfileActivity",
                                                        message
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }
        })
    }
}