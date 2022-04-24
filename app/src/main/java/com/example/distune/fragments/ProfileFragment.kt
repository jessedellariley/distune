package com.example.distune.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.distune.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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