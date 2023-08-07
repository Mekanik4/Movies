package com.example.movies.utils

class Constants {
    companion object {
        const val SHARED_PREFERENCES_NAME = "Movies_Shared_Preferences"
        const val CONNECTED = "CONNECTED"
        const val USER_ID = "USERID"
        val USER_MAP = HashMap<String,String>().apply {
            put("test1", "12345")
            put("test2", "12345")
        }
    }
}