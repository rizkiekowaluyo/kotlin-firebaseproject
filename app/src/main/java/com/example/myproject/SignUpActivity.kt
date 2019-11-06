package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity: AppCompatActivity(), View.OnClickListener {
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signup)
        btnSignUp.setOnClickListener(this)
        tvBackLogin.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {
        val i = v.id
        when(i){
            R.id.btnSignUp -> signUpEmailAndPassword(emailFieldRegister.text.toString(),passwordFieldRegister.text.toString())
            R.id.tvBackLogin -> startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
        }
    }

    private fun signUpEmailAndPassword(email: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}