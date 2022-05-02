package com.example.distune.fragments

import android.content.Intent
import android.os.Bundle
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
import com.parse.ParseUser

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_username).text = ParseUser.getCurrentUser().username
        view.findViewById<TextView>(R.id.tv_bio).text = ParseUser.getCurrentUser().getString("bio")

        view.findViewById<ImageView>(R.id.ivEditProfile).setOnClickListener() {
            var i = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(i)
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