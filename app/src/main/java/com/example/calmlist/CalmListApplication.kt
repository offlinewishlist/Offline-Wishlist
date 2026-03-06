package com.example.calmlist

import android.app.Application
import android.util.Log

class CalmListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("DEBUG_APP", "CalmListApplication onCreate started")
        
        try {
            Log.e("DEBUG_APP", "CalmListApplication initialized successfully")
        } catch (e: Exception) {
            Log.e("DEBUG_APP", "CRASH IN APPLICATION ONCREATE", e)
        }
    }
}
