package com.example.distune

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.distune.fragments.usersToDiscover
import com.parse.*

class FollowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_profile)

        lateinit var user: ParseUser
        var username = intent.getStringExtra(FOLLOWER_EXTRA)
        val query: ParseQuery<ParseUser> = ParseUser.getQuery()
        query.whereEqualTo("username", username)
        query.findInBackground(object : FindCallback<ParseUser> {
            override fun done(results: MutableList<ParseUser>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e("FollowProfileActivity", "Error loading users")
                    e.printStackTrace()
                } else {
                    if (results != null && results.size > 0) {
                        for (result in results) {
                            user = result
                        }
                    }
                }
            }
        })
    }
}