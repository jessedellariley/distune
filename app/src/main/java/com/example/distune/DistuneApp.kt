package com.example.distune

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseUser

class DistuneApp: Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Playlist::class.java)
        ParseObject.registerSubclass(Track::class.java)
        ParseObject.registerSubclass(Favorite::class.java)
        ParseObject.registerSubclass(Follower::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }
}