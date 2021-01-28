package com.example.myyoutubever2

import android.app.Application
import com.example.myyoutubever2.database.AppDatabase

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        AppDatabase.createDatebase(this)
    }
}