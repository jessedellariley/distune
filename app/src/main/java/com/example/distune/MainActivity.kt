package com.example.distune

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.distune.fragments.DiscoverFragment
import com.example.distune.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_discover -> {
                    fragmentToShow = DiscoverFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
                R.id.action_logout -> {

                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flMainContainer,fragmentToShow).commit()
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }

        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.action_discover
    }
}