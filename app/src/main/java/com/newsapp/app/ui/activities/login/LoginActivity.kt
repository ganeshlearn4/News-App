package com.newsapp.app.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.newsapp.BuildConfig
import com.newsapp.R
import com.newsapp.app.ui.activities.home.HomeActivity
import com.newsapp.data.model.User
import com.newsapp.data.prefs.PrefsHelper
import com.newsapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    lateinit var googleSignInClient: GoogleSignInClient

    private var activityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val googleSignInTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val userAccount = googleSignInTask.getResult(ApiException::class.java)
                PrefsHelper.saveUserLogin(
                    User(
                        userAccount.displayName!!,
                        userAccount.email!!,
                        userAccount.photoUrl.toString()
                    )
                )

                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()

                Toast.makeText(this, "Sign In successful", Toast.LENGTH_LONG).show()
            } catch (e: ApiException) {
                Toast.makeText(this, "Error while Signing In", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Error while Signing In", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_SIGN_IN_WEB_CLIENT_ID)
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        setListeners()
    }

    private fun setListeners() {
        binding.googleSignInButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val googleSignInIntent = googleSignInClient.signInIntent
        activityResultLauncher.launch(googleSignInIntent)
    }
}