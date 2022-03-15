package com.newsapp.app.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.newsapp.app.ui.activities.home.HomeActivity
import com.newsapp.app.ui.activities.login.LoginActivity
import com.newsapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        Handler().postDelayed({
            if (account == null) {
                val signInIntent = Intent(this, LoginActivity::class.java)
                startActivity(signInIntent)
                finish()
            } else {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }
        }, 1000)
    }
}