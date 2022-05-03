package com.example.distune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.distune.fragments.DiscoverFragment
import com.example.distune.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var usersToDiscover = intent.getStringExtra("USERS_TO_DISCOVER")
        var bundle = Bundle()
        bundle.putString("USERS_TO_DISCOVER",usersToDiscover)

        var fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")

        val fragmentManager: FragmentManager = supportFragmentManager

        if (fragmentToLoad.equals("profile")) {
            fragmentManager.beginTransaction().replace(R.id.flMainContainer,ProfileFragment()).commit()
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.action_profile
        } else {
            // Set default selection
            var fragment = DiscoverFragment()
            fragment.arguments = bundle
            fragmentManager.beginTransaction().replace(R.id.flMainContainer,fragment).commit()
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.action_discover
        }

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_discover -> {
                    fragmentToShow = DiscoverFragment()
                    fragmentToShow.arguments = bundle
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
                R.id.action_logout -> {
                    ParseUser.logOutInBackground()
                    val i = Intent(this,LoginActivity::class.java)
                    i.putExtra("LOGGED_OUT","logged_out")
                    startActivity(i)
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flMainContainer,fragmentToShow).commit()
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }

    }
}