package com.example.a5046a3.data.manager

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages saving / retrieving the currently-logged Firebase uid in SharedPreferences so that
 * local components (Room, ViewModels) can work with the current user without coupling to
 * FirebaseAuth directly.
 */
class UserManager private constructor(context: Context) {

    companion object {
        private const val PREFS = "user_prefs"
        private const val KEY_UID = "uid"
        @Volatile private var INSTANCE: UserManager? = null
        fun getInstance(context: Context): UserManager = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserManager(context.applicationContext).also { INSTANCE = it }
        }
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun saveUserId(uid: String) = prefs.edit().putString(KEY_UID, uid).apply()
    fun getUserId(): String? = prefs.getString(KEY_UID, null)
    fun isLoggedIn(): Boolean = getUserId() != null
    fun clearUserId() = prefs.edit().remove(KEY_UID).apply()
} 