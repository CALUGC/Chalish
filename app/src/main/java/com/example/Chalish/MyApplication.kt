package com.example.Chalish
import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import okhttp3.WebSocket
import kotlin.concurrent.thread

class MyApplication: Application() {
    companion object {
        lateinit var firebaseUser : FirebaseUser
    }
}