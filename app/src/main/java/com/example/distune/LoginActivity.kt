package com.example.distune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.parse.*
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import okhttp3.*
import java.io.IOException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "fcc35d2c1acf43208e5e83d87e89b4e1"
    private val REDIRECT_URI = "distune-login://callback"
    private val REQUEST_CODE = 3928

    private val CURRENT_USER_PROFILE_URL = "https://api.spotify.com/v1/me"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (intent.getStringExtra("LOGGED_OUT") == null) {
            loginViaSpotify()
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener() {
            loginViaSpotify()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            var response = AuthorizationClient.getResponse(resultCode, data)
            var type = response.type
            if (type.equals(AuthorizationResponse.Type.TOKEN)) {
                Log.d("LoginActivity", "User logged into Spotify successfully")
                val token = response.accessToken
                // Request Spotify profile of current user
                var client = OkHttpClient()
                val request = Request.Builder()
                    .addHeader("Authorization", "Bearer $token")
                    .url(CURRENT_USER_PROFILE_URL)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
                            val responseBody = JSONObject(response.body()!!.string())
                            var username = responseBody.getString("display_name")
                            var spotifyId = responseBody.getString("id")
                            Log.d("LoginActivity", "Retrieved Spotify account info for $username")
                            // Use Spotify profile to login or sign up ParseUser
                            loginUser(username,spotifyId,spotifyId,token)
                            goToMainActivity()
                        }
                    }
                })
            } else if (type.equals(AuthorizationResponse.Type.ERROR)) {
                var error = response.error
                Log.e("LoginActivity", error)
            } else {
                Log.d("LoginActivity", "Auth flow cancelled")
            }
        }
    }

    private fun signUpUser(username: String,
                           password: String,
                           spotifyId: String,
                           accessToken: String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)
        user.put("token", accessToken)
        user.put("spotifyId", spotifyId)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                Log.d("LoginActivity", "Successfully created ParseUser")
            } else {
                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String,
                          password: String,
                          spotifyId: String,
                          accessToken: String) {
        Log.d("LoginActivity", "Logging in parse user")
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.d("LoginActivity", "Successfully logged in Parse User")
                ParseUser.getCurrentUser().put("token",accessToken)
                user.saveInBackground {
                    if (it != null) {
                        it.localizedMessage?.let { message ->
                            Log.e(
                                "LoginActivity",
                                message
                            )
                        }
                    }
                }
            } else {
                signUpUser(username,password,spotifyId,accessToken)
                e.printStackTrace()
            }})
        )
    }

    private fun loginViaSpotify() {
        var builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(Array<String>(1){"user-top-read,user-library-read,user-read-email,user-read-private,playlist-read-private"})
        var request = builder.build()
        Log.d("LoginActivity", "Opening Spotify login activity")
        AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request)
    }

    private fun goToMainActivity() {
        val i = Intent(this@LoginActivity, SplashActivity::class.java)
        startActivity(i)
        finish()
    }
}