package com.example.distune.fragments

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.distune.Follower
import com.example.distune.R
import com.google.android.material.button.MaterialButton
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.Track
import org.json.JSONArray

lateinit var usersToDiscover : JSONArray
private val CLIENT_ID = "fcc35d2c1acf43208e5e83d87e89b4e1"
private val REDIRECT_URI = "distune-login://callback"
private var spotifyAppRemote: SpotifyAppRemote? = null

class DiscoverFragment : Fragment() {

    lateinit var noUsers: TextView
    lateinit var constraintLayout: ConstraintLayout
    lateinit var swipeRight: ImageView
    lateinit var swipeLeft: ImageView
    lateinit var username: TextView
    lateinit var bio: TextView
    lateinit var discover1Title: TextView
    lateinit var discover1Album: ImageView
    lateinit var discover1Artist: TextView
    lateinit var discover2Title: TextView
    lateinit var discover2Album: ImageView
    lateinit var discover2Artist: TextView
    lateinit var discover3Title: TextView
    lateinit var discover3Album: ImageView
    lateinit var discover3Artist: TextView
    lateinit var discover1: ConstraintLayout
    lateinit var discover2: ConstraintLayout
    lateinit var discover3: ConstraintLayout
    lateinit var toggle: ImageView
    lateinit var discoverListen: ConstraintLayout
    lateinit var discoverListenTitle: TextView
    lateinit var discoverListenAlbum: ImageView
    lateinit var discoverListenArtist: TextView
    var i = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var users = arguments?.getString("USERS_TO_DISCOVER")
        usersToDiscover = JSONArray(users)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noUsers = view.findViewById(R.id.noUsers)
        constraintLayout = view.findViewById(R.id.constraintLayout)
        swipeRight = view.findViewById(R.id.swipeRight)
        swipeLeft = view.findViewById(R.id.swipeLeft)
        username = view.findViewById(R.id.tv_username)
        bio = view.findViewById(R.id.tv_bio)
        // var image = view.findViewById<ImageView>(R.id.iv_profile_image)
        discover1Title = view.findViewById(R.id.discover1Title)
        discover1Album = view.findViewById(R.id.discover1Album)
        discover1Artist = view.findViewById(R.id.discover1Artist)
        discover2Title = view.findViewById(R.id.discover2Title)
        discover2Album = view.findViewById(R.id.discover2Album)
        discover2Artist = view.findViewById(R.id.discover2Artist)
        discover3Title = view.findViewById(R.id.discover3Title)
        discover3Album = view.findViewById(R.id.discover3Album)
        discover3Artist = view.findViewById(R.id.discover3Artist)
        discover1 = view.findViewById(R.id.discover1)
        discover2 = view.findViewById(R.id.discover2)
        discover3 = view.findViewById(R.id.discover3)
        toggle = view.findViewById(R.id.toggle)
        discoverListen = view.findViewById(R.id.discoverListen)
        discoverListenTitle = view.findViewById(R.id.discoverListenTitle)
        discoverListenAlbum = view.findViewById(R.id.discoverListenAlbumThumbnail)
        discoverListenArtist = view.findViewById(R.id.discoverListenArtist)

        i = 0
        if (usersToDiscover.length() > 0) {
            // Load first user
            Log.d("DiscoverFragment", "Attempting to load user")
            username.text = usersToDiscover.getJSONObject(i).getString("username")
            bio.text = usersToDiscover.getJSONObject(i).getString("bio")
            //Glide.with(requireContext()).load(usersToDiscover.getJSONObject(i).getString("image")).into(image)
            discover1Title.text = usersToDiscover
                .getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(0)
                .getString("title")
            discover1Artist.text = usersToDiscover
                .getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(0)
                .getString("artist")
            Glide.with(requireContext())
                .load(
                    usersToDiscover.getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(0)
                        .getString("image")
                )
                .into(discover1Album)
            discover2Title.text = usersToDiscover
                .getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(1)
                .getString("title")
            discover2Artist.text = usersToDiscover
                .getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(1)
                .getString("artist")
            Glide.with(requireContext())
                .load(
                    usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(1)
                        .getString("image")
                )
                .into(discover2Album)
            discover3Title.text = usersToDiscover
                .getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(2)
                .getString("title")
            discover3Artist.text = usersToDiscover
                .getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(2)
                .getString("artist")
            Glide.with(requireContext())
                .load(
                    usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(2)
                        .getString("image")
                )
                .into(discover3Album)

            // On swipe right
            swipeRight.setOnClickListener() {
                // follow user so that content can be accessed later
                var follower = Follower()
                follower.setFollower(ParseUser.getCurrentUser())
                val query: ParseQuery<ParseUser> = ParseUser.getQuery()
                query.whereEqualTo("username", usersToDiscover.getJSONObject(i).getString("username"))
                query.findInBackground(object : FindCallback<ParseUser> {
                    override fun done(results: MutableList<ParseUser>?, e: ParseException?) {
                        if (e != null) {
                            // Something has gone wrong
                            Log.e("DiscoverFragment", "Error loading users")
                            e.printStackTrace()
                        } else {
                            if (results != null && results.size > 0) {
                                for (result in results) {
                                    follower.setUser(result)
                                    follower.saveInBackground {
                                        if (it != null) {
                                            it.localizedMessage?.let { message ->
                                                Log.e(
                                                    "PlaylistsFragment",
                                                    message
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
                // increase i and check if valid
                i++
                // if valid, load another user
                if (i < usersToDiscover.length()) {
                    Log.d("DiscoverFragment", "Attempting to load user")
                    username.text = usersToDiscover.getJSONObject(i).getString("username")
                    bio.text = usersToDiscover.getJSONObject(i).getString("bio")
                    //Glide.with(requireContext()).load(usersToDiscover.getJSONObject(i).getString("image")).into(image)
                    discover1Title.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(0)
                        .getString("title")
                    discover1Artist.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(0)
                        .getString("artist")
                    Glide.with(requireContext())
                        .load(
                            usersToDiscover
                                .getJSONObject(i)
                                .getJSONArray("favorites")
                                .getJSONObject(0)
                                .getString("image")
                        )
                        .into(discover1Album)
                    discover2Title.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(1)
                        .getString("title")
                    discover2Artist.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(1)
                        .getString("artist")
                    Glide.with(requireContext())
                        .load(
                            usersToDiscover
                                .getJSONObject(i)
                                .getJSONArray("favorites")
                                .getJSONObject(1)
                                .getString("image")
                        )
                        .into(discover2Album)
                    discover3Title.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(2)
                        .getString("title")
                    discover3Artist.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(2)
                        .getString("artist")
                    Glide.with(requireContext())
                        .load(
                            usersToDiscover
                                .getJSONObject(i)
                                .getJSONArray("favorites")
                                .getJSONObject(2)
                                .getString("image")
                        )
                        .into(discover3Album)
                    // else, make everything invisible and show a text view saying no new users to discover
                } else {
                    constraintLayout.visibility = View.INVISIBLE
                    swipeRight.visibility = View.INVISIBLE
                    swipeLeft.visibility = View.INVISIBLE
                    noUsers.visibility = View.VISIBLE
                }
            }

            // On swipe left
            swipeLeft.setOnClickListener() {
                // increase i and check if valid
                i++
                // if valid, load another user
                if (i < usersToDiscover.length()) {
                    Log.d("DiscoverFragment", "Attempting to load user")
                    username.text = usersToDiscover.getJSONObject(i).getString("username")
                    bio.text = usersToDiscover.getJSONObject(i).getString("bio")
                    //Glide.with(requireContext()).load(usersToDiscover.getJSONObject(i).getString("image")).into(image)
                    discover1Title.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(0)
                        .getString("title")
                    discover1Artist.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(0)
                        .getString("artist")
                    Glide.with(requireContext())
                        .load(
                            usersToDiscover
                                .getJSONObject(i)
                                .getJSONArray("favorites")
                                .getJSONObject(0)
                                .getString("image")
                        )
                        .into(discover1Album)
                    discover2Title.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(1)
                        .getString("title")
                    discover2Artist.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(1)
                        .getString("artist")
                    Glide.with(requireContext())
                        .load(
                            usersToDiscover
                                .getJSONObject(i)
                                .getJSONArray("favorites")
                                .getJSONObject(1)
                                .getString("image")
                        )
                        .into(discover2Album)
                    discover3Title.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(2)
                        .getString("title")
                    discover3Artist.text = usersToDiscover
                        .getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(2)
                        .getString("artist")
                    Glide.with(requireContext())
                        .load(
                            usersToDiscover
                                .getJSONObject(i)
                                .getJSONArray("favorites")
                                .getJSONObject(2)
                                .getString("image")
                        )
                        .into(discover3Album)
                    // else, make everything invisible and show a text view saying no new users to discover
                } else {
                    constraintLayout.visibility = View.INVISIBLE
                    swipeRight.visibility = View.INVISIBLE
                    swipeLeft.visibility = View.INVISIBLE
                    noUsers.visibility = View.VISIBLE
                }
            }
        } else {
            // Make everything invisible and show a text view saying no new users to discover
            constraintLayout.visibility = View.INVISIBLE
            swipeRight.visibility = View.INVISIBLE
            swipeLeft.visibility = View.INVISIBLE
            noUsers.visibility = View.VISIBLE
        }
    }

    // The following three functions handle the audio player
    override fun onStart() {
        super.onStart()

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(requireContext(), connectionParams, object: Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("DiscoverFragment", "Connected! Yay!")
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("DiscoverFragment", throwable.message, throwable)
            }
        })
    }

    private fun connected() {
        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            discoverListenTitle.text = track.name
            discoverListenArtist.text = track.artist.name
        }

        toggle.setOnClickListener() {
            if (toggle.isActivated) { // pause button showing
                toggle.isActivated = !toggle.isActivated
                spotifyAppRemote!!.playerApi.pause()
            } else { // play button showing
                toggle.isActivated = !toggle.isActivated
                spotifyAppRemote!!.playerApi.resume()
            }
        }

        discover1.setOnClickListener() {
            discoverListen.visibility = View.VISIBLE
            discoverListenTitle.text = discover1Title.text
            discoverListenArtist.text = discover1Artist.text
            Glide.with(requireContext())
                .load(
                    usersToDiscover.getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(0)
                        .getString("image")
                )
                .into(discoverListenAlbum)
            var spotifyId = usersToDiscover.getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(0)
                .getString("spotifyId")
            // Start playing playlist
            spotifyAppRemote!!.playerApi.play("spotify:track:$spotifyId")
            // Toggle bottom audio bar button to pause
            if (!toggle.isActivated)
                toggle.isActivated = !toggle.isActivated
        }

        discover2.setOnClickListener() {
            discoverListen.visibility = View.VISIBLE
            discoverListenTitle.text = discover2Title.text
            discoverListenArtist.text = discover2Artist.text
            Glide.with(requireContext())
                .load(
                    usersToDiscover.getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(1)
                        .getString("image")
                )
                .into(discoverListenAlbum)
            var spotifyId = usersToDiscover.getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(1)
                .getString("spotifyId")
            // Start playing playlist
            spotifyAppRemote!!.playerApi.play("spotify:track:$spotifyId")
            // Toggle bottom audio bar button to pause
            if (!toggle.isActivated)
                toggle.isActivated = !toggle.isActivated
        }

        discover3.setOnClickListener() {
            discoverListen.visibility = View.VISIBLE
            discoverListenTitle.text = discover3Title.text
            discoverListenArtist.text = discover3Artist.text
            Glide.with(requireContext())
                .load(
                    usersToDiscover.getJSONObject(i)
                        .getJSONArray("favorites")
                        .getJSONObject(2)
                        .getString("image")
                )
                .into(discoverListenAlbum)
            var spotifyId = usersToDiscover.getJSONObject(i)
                .getJSONArray("favorites")
                .getJSONObject(2)
                .getString("spotifyId")
            // Start playing playlist
            spotifyAppRemote!!.playerApi.play("spotify:track:$spotifyId")
            // Toggle bottom audio bar button to pause
            if (!toggle.isActivated)
                toggle.isActivated = !toggle.isActivated
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}