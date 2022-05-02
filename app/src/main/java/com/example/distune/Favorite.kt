package com.example.distune

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Favorite")
class Favorite : ParseObject() {

    fun getId(): String? {
        return getString(KEY_ID)
    }

    fun setId(id: String) {
        put(KEY_ID, id)
    }

    fun getTitle(): String? {
        return getString(KEY_TITLE)
    }

    fun setTitle(name: String) {
        put(KEY_TITLE, name)
    }

    fun getImage(): String? {
        return getString(KEY_IMAGE)
    }

    fun setImage(image: String) {
        put(KEY_IMAGE, image)
    }

    fun getArtist() : String? {
        return getString(KEY_ARTIST)
    }

    fun setArtist(artist : String) {
        put(KEY_ARTIST,artist)
    }

    fun getUser() : ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(playlist:ParseUser) {
        put(KEY_USER,playlist)
    }

    fun getIndex() : Number? {
        return getNumber(KEY_INDEX)
    }

    fun setIndex(i : Number) {
        put(KEY_INDEX,i)
    }

    companion object {
        const val KEY_ID = "spotifyId"
        const val KEY_TITLE = "title"
        const val KEY_IMAGE = "image"
        const val KEY_ARTIST = "artist"
        const val KEY_USER = "user"
        const val KEY_INDEX = "index"
    }
}