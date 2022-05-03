package com.example.distune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.distune.fragments.DiscoverFragment
import com.example.distune.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseUser

class FollowProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_profile)

        findViewById<BottomNavigationView>(R.id.profileBottomNav).selectedItemId = R.id.action_invisible

        var username = intent.getStringExtra(FOLLOWER_EXTRA)

        var bundle = Bundle()
        bundle.putString("WHICH_USER", username)
        var fragment = ProfileFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.followProfileContainer,fragment).commit()

        findViewById<BottomNavigationView>(R.id.profileBottomNav).setOnItemSelectedListener {
                item ->
            when(item.itemId) {

                R.id.action_discover -> {
                    val i = Intent(this,SplashActivity::class.java)
                    startActivity(i)
                }
                R.id.action_profile -> {
                    val i = Intent(this,MainActivity::class.java)
                    i.putExtra("FRAGMENT_TO_LOAD", "profile")
                    startActivity(i)
                }
                R.id.action_logout -> {
                    ParseUser.logOutInBackground()
                    val i = Intent(this,LoginActivity::class.java)
                    i.putExtra("LOGGED_OUT","logged_out")
                    startActivity(i)
                }
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }
    }
}