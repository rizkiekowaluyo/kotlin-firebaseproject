package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FacebookAuthProvider


class SignInActivity: AppCompatActivity(), View.OnClickListener {

    var auth: FirebaseAuth? = null
    lateinit var googleSignInClient: GoogleSignInClient
    //private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin)

        btnLogin.setOnClickListener(this)
        btn_login_gmail.setOnClickListener(this)
        btn_login_facebook.setOnClickListener(this)
        signUpCall.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        //callbackManager = CallbackManager.Factory.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAGGoogle, "Google sign in failed", e)
                updateUI(null)
            }
        }else if (requestCode == RC_SIGN_IN_FACEBOOK){
           // callbackManager.onActivityResult(RC_SIGN_IN_FACEBOOK,resultCode,data)
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
            R.id.btnLogin -> signInWithEmailAndPassword(emailFieldLogin.text.toString(),passwordFieldLogin.text.toString())
            R.id.btn_login_gmail -> signInWithGmail()
            R.id.btn_login_facebook -> signInWithFacebook()
            R.id.signUpCall -> startActivity(Intent(this@SignInActivity,SignUpActivity::class.java))
        }
    }

    private fun signInWithFacebook() {
//        Log.d(TAGFacebook, "handleFacebookAccessToken:$token")
//        // [START_EXCLUDE silent]
//        progBar.visibility = View.VISIBLE
//
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        auth!!.signInWithCredential(credential)
//            .addOnCompleteListener(this){task ->
//                if (task.isSuccessful){
//                    Log.d(TAGFacebook, "signInWithCredential:success")
//                    val user = auth!!.currentUser
//                    updateUI(user)
//                }else{
//                    Log.w(TAGFacebook, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
//                }
//            }
    }

    private fun signInWithGmail() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth!!.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful) {
                    Log.d(TAGEmail, "signInWithEmail:success")
                    btnLogin.visibility = View.GONE
                    progBar.visibility = View.VISIBLE
                    btn_login_gmail.visibility = View.GONE
                    btn_login_facebook.visibility = View.GONE
                    val user = auth!!.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAGEmail, "signInWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAGGoogle, "firebaseAuthWithGoogle:" + acct.id!!)

        progBar.visibility = View.VISIBLE

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAGGoogle, "signInWithCredential:success")
                    btn_login_gmail.visibility = View.GONE
                    btn_login_facebook.visibility = View.GONE
                    val user = auth!!.currentUser
                    updateUI(user)
                } else {
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
        private const val RC_SIGN_IN_FACEBOOK = 9002
    }
}