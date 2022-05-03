package com.example.distune.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.distune.EditProfileActivity
import com.example.distune.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import java.util.*
import kotlin.concurrent.schedule

class ProfileFragment : Fragment() {
    lateinit var userForProfile : ParseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var username = arguments?.getString("WHICH_USER")
        val query: ParseQuery<ParseUser> = ParseUser.getQuery()
        query.whereEqualTo("username", username)
        var results: MutableList<ParseUser>? = query.find()
        userForProfile = results!![0]
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_username).text = userForProfile?.username
        view.findViewById<TextView>(R.id.tv_bio).text = userForProfile?.getString("bio")

        if (ParseUser.getCurrentUser().equals(userForProfile)) {
            view.findViewById<ImageView>(R.id.ivEditProfile).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivEditProfile).setOnClickListener() {
                var i = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(i)
            }
        }

        val fragmentManager: FragmentManager = childFragmentManager

        view.findViewById<BottomNavigationView>(R.id.nvProfile).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_playlists -> {
                    fragmentToShow = PlaylistFragment()
                }
                R.id.action_followers -> {
                    fragmentToShow = FollowerFragment()
                }
                R.id.action_following -> {
                    fragmentToShow = FollowingFragment()
                }
            }

            var bundle = Bundle()
            bundle.putString("WHICH_USER", userForProfile?.username)
            fragmentToShow?.arguments = bundle

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flProfileContainer,fragmentToShow).commit()
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }

        // Set default selection
        view.findViewById<BottomNavigationView>(R.id.nvProfile).selectedItemId = R.id.action_playlists
    }
}