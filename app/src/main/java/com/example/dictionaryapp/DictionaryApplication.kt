package com.example.dictionaryapp

import android.app.Application
import com.example.dictionaryapp.data.room.AppDatabase

class DictionaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppDatabase.init(this)


    }
}