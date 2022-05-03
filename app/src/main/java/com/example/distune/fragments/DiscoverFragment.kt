package com.example.distune.fragments

import android.os.Bundle
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
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import org.json.JSONArray

lateinit var usersToDiscover : JSONArray

class DiscoverFragment : Fragment() {

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

        var noUsers = view.findViewById<TextView>(R.id.noUsers)
        var constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraintLayout)
        var swipeRight = view.findViewById<ImageView>(R.id.swipeRight)
        var swipeLeft = view.findViewById<ImageView>(R.id.swipeLeft)
        var username = view.findViewById<TextView>(R.id.tv_username)
        var bio = view.findViewById<TextView>(R.id.tv_bio)
        // var image = view.findViewById<ImageView>(R.id.iv_profile_image)
        var discover1Title = view.findViewById<TextView>(R.id.discover1Title)
        var discover1Album = view.findViewById<ImageView>(R.id.discover1Album)
        var discover1Artist = view.findViewById<TextView>(R.id.discover1Artist)
        var discover2Title = view.findViewById<TextView>(R.id.discover2Title)
        var discover2Album = view.findViewById<ImageView>(R.id.discover2Album)
        var discover2Artist = view.findViewById<TextView>(R.id.discover2Artist)
        var discover3Title = view.findViewById<TextView>(R.id.discover3Title)
        var discover3Album = view.findViewById<ImageView>(R.id.discover3Album)
        var discover3Artist = view.findViewById<TextView>(R.id.discover3Artist)

        var i = 0
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
}