package com.safacet.tradetracker.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.safacet.tradetracker.R

class LoginActivity : AppCompatActivity() {

    companion object {
        const val SIGN_IN_TAG= "SIGN_IN_TAG"
        const val SIGN_UP_TAG= "SIGN_UP_TAG"
    }

    private lateinit var signInFragment: SignInFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        signInFragment = SignInFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.flLogin, signInFragment, SIGN_IN_TAG).commit()
    }

    fun signUp() {
        val signUpFragment = SignUpFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.flLogin, signUpFragment, SIGN_UP_TAG).commit()
    }

    fun onBackButtonClicked() {
        val fragment = supportFragmentManager.findFragmentByTag(SIGN_UP_TAG)
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragment).commit()
        }
    }

    fun openHomePage() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}