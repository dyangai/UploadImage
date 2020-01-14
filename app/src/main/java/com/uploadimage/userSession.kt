package com.uploadimage

import android.content.Context

class userSession {
    lateinit var c: Context
    private val PREF_NAME = "savedStatus"
    private val KEY_ID = "KEY_ID"

    fun setStatus(c: Context, s: String){
        val prefs = c.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_ID,s)
        editor.apply()
    }

    fun getStatus(c: Context): String? {
        val prefs = c.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        return prefs.getString(KEY_ID, "")
    }


}