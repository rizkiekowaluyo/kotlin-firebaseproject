package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        aviIndicator.setIndicator("BallPulseIndicator")
        Handler().postDelayed({
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
            finish()
        },2500)
    }
}