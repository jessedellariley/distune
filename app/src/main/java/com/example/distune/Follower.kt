package com.example.distune

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Follower")
class Follower : ParseObject() {
    fun getUser() : ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser) {
        put(KEY_USER,user)
    }

    fun getFollower() : ParseUser? {
        return getParseUser(KEY_FOLLOWER)
    }

    fun setFollower(user : ParseUser) {
        put(KEY_FOLLOWER,user)
    }

    companion object {
        const val KEY_USER = "user"
        const val KEY_FOLLOWER = "follower"
    }
}