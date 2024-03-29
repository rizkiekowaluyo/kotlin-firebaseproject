package com.example.myproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var PICK_IMAGE_REQUEST = 1
    lateinit var imageBitmap: Bitmap
    var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        tvText.text = user?.email

        findViewById<ImageView>(R.id.imgPict)
        FirebaseApp.initializeApp(this)


        btn_camera.setOnClickListener { takePictureIntent() }
        btn_detect.setOnClickListener { detectImage() }
    }

    private fun detectImage() {
        val image = FirebaseVisionImage.fromBitmap(imageBitmap) as FirebaseVisionImage
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText -> processTextRecognitionResult(firebaseVisionText) }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null){
            imageBitmap = data.extras?.get("data") as Bitmap
            imgPict.setImageBitmap(imageBitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setActionMenu(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setActionMenu(selectedMenu: Int) {
        when(selectedMenu){
            R.id.action_signout -> {
                mAuth!!.signOut()
                LoginManager.getInstance().logOut()
                startActivity(Intent(this@MainActivity,SignInActivity::class.java))
                finish()
                DynamicToast.makeSuccess(applicationContext,"Sign Out Successfully").show()
            }
        }
    }

    private fun processTextRecognitionResult(firebaseVisionText: FirebaseVisionText) {
        val blocks = firebaseVisionText.textBlocks
        if (blocks.size == 0){
            Toast.makeText(this,"No Text",Toast.LENGTH_LONG).show()
            return
        }
        for (block in firebaseVisionText.textBlocks){
            val txt = block.text
            tvText.setText(txt)
        }
    }

    private fun takePictureIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST)
    }


}
