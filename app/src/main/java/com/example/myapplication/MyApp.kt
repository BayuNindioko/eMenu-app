package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import java.util.prefs.Preferences

class MyApp : Application() {
    private val Context.dataStore by preferencesDataStore(name = "settings")
}
