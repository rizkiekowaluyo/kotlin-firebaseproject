package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity: AppCompatActivity(), View.OnClickListener {

    var auth: FirebaseAuth? = null
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)

        btnLogin.setOnClickListener(this)
        btn_login_gmail.setOnClickListener(this)
        btn_login_facebook.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAGGoogle, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth?.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            Handler().postDelayed({
                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }

    }

    override fun onClick(v: View) {
        val i = v.id

        when(i){
            R.id.btnLogin -> signInWithEmailAndPassword()
            R.id.btn_login_gmail -> signInWithGmail()
            R.id.btn_login_facebook -> signInWithFacebook()
        }
    }

    private fun signInWithFacebook() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun signInWithGmail() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
    }

    private fun signInWithEmailAndPassword() {

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAGGoogle, "firebaseAuthWithGoogle:" + acct.id!!)

        progBar.visibility = View.VISIBLE

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAGGoogle, "signInWithCredential:success")
                    btn_login_gmail.visibility = View.GONE
                    btn_login_facebook.visibility = View.GONE
                    val user = auth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAGGoogle, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }



    companion object {
        private const val TAGGoogle = "GoogleActivity"
        private const val TAGFacebook = "FacebookActivity"
        private const val TAGEmail = "EmailActivity"

        private const val RC_SIGN_IN_GOOGLE = 9001
    }
}