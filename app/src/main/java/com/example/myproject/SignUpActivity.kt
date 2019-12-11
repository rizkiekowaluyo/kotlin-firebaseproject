package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        auth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext,"Succesfully Created",Toast.LENGTH_SHORT).show()
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object{
        private  const val TAG = "CreateUser"
    }
}