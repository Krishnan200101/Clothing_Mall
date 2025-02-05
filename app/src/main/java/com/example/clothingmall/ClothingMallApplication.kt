package com.example.clothingmall

import android.app.Application
import com.google.firebase.FirebaseApp

class ClothingMallApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
} 