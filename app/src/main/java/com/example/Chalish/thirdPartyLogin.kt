package com.example.Chalish

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


class thirdPartyLogin : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private lateinit var firebaseUser : FirebaseUser

    val authProvider: List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_party_login)
        Log.d("callt", "onCreate: ")
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(authProvider)
                .setIsSmartLockEnabled(false)
                .setAlwaysShowSignInMethodScreen(true)
                .build(),
            RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("calltoResult","imback $resultCode")
        if (requestCode == this.RC_SIGN_IN) {
            Log.d("calltoResult","imin $resultCode")
            if (resultCode != Activity.RESULT_OK) {
                val response = IdpResponse.fromResultIntent(data)
                Toast.makeText(applicationContext, response?.error?.errorCode.toString(), Toast.LENGTH_SHORT).show()
            }
            this.firebaseUser = FirebaseAuth.getInstance().currentUser!!
            funSqlInsertUser()
        }
    }

    fun funStoredLoginInfo() {
//        val txtUserName = findViewById<TextView>(R.id.txtUserName)
//        txtUserName.text = Companion.firebaseUser.getDisplayName()

        val name = this.firebaseUser.getDisplayName()
        //取得使用者頭像位址
        val photoUrl = this.firebaseUser.getPhotoUrl()
        //取得使用者 uid
        val uid = this.firebaseUser.getUid()

        Log.d("userinfo", "$name")
        Log.d("userinfo", "$photoUrl")
        Log.d("userinfo", "$uid")

        val settings = getSharedPreferences("DATA", 0)
        settings.edit()
            .putString("StrUsername",this.firebaseUser.getDisplayName() )
            .putString("StrPhotoUrl", this.firebaseUser.getPhotoUrl().toString())
            .putString("StrUid", this.firebaseUser.getUid())
            .apply()


//        val job: Job = GlobalScope.launch{
//            while (FirebaseAuth.getInstance().currentUser != null){
//                Log.d("user status update ", FirebaseAuth.getInstance().currentUser!!.displayName!!)
//                val url = "http://140.136.151.173:3000/query/updateUserStatus/"+FirebaseAuth.getInstance().currentUser!!.uid
//                val jsonStr = URL(url).readText()
//                Log.d("user status update res", jsonStr.toString())
//                delay(5000L)
//            }
//        }
//        job.start()

        val intent = Intent(this, WsConnect::class.java)
        startActivity(intent)

    }

    private fun funSqlInsertUser(){
        val database = FirebaseDatabase.getInstance()
        val strUid = this.firebaseUser.uid
        val ref = database.getReference("users/"+strUid+"/profiles")
        ref.child("name").setValue(this.firebaseUser.displayName)
        ref.child("photoUrl").setValue(this.firebaseUser.photoUrl.toString())
//        thread {
//            var urlName = URLEncoder.encode(this.firebaseUser.getDisplayName(),"utf-8")
//            val urlPhotoUrl = Base64.encodeToString(this.firebaseUser.getPhotoUrl().toString().toByteArray(), Base64.DEFAULT);
//            Log.d("insert", urlPhotoUrl)
//            var url = "http://140.136.151.173:3000/query/insertUser/"+this.firebaseUser.getUid()+"/"+urlName+"/"+urlPhotoUrl
//            val jsonStr = URL(url).readText()
//            Log.d("insert", jsonStr)
            funStoredLoginInfo()
//        }
    }
}
