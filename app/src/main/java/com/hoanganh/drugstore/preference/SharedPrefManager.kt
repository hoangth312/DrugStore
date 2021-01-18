package com.hoanganh.drugstore.preference

import android.content.Context
import android.content.SharedPreferences
import com.hoanganh.drugstore.Model.User


class SharedPrefManager private constructor(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(GET_USER, Context.MODE_PRIVATE)
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = context.getSharedPreferences(GET_USER, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    fun logOutShare(){
        sharedPreferences.edit().clear().apply()
    }

    fun saveUser(user: User) {

        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("email", user.email)
        editor.putString("token", user.token)
        editor.putString("userName", user.userName)
        editor.putString("type", user.type)
        editor.putString("firstName", user.firstName)
        editor.putString("lastName", user.lastName)
        editor.putString("avatarUrl", user.avatarUrl)
        editor.putString("passwordEncode", user.passwordEncode)
        editor.apply()
    }




       fun getID(): Int { return sharedPreferences.getInt("id", 0)}
      fun getEmail(): String? {return sharedPreferences.getString("email", "")}
        fun getToken (): String? {return sharedPreferences.getString("token", "")}
        fun getUserName(): String?  {return sharedPreferences.getString("userName", "")}
        fun getType(): String?  {return sharedPreferences.getString("type", "")}
        fun getFirstName(): String?  {return sharedPreferences.getString("firstName", "")}
        fun getLastName(): String?  {return sharedPreferences.getString("lastName", "")}
        fun getAvatarUrl(): String?  {return sharedPreferences.getString("avatarUrl", "")}
        fun getPasswordEncode(): String?  {return sharedPreferences.getString("passwordEncode", "")}




    companion object {
        val GET_USER = "getUser"
        val SET_USER = "setUser"
        private var sharedPrefManager: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context?): SharedPrefManager {
            if (sharedPrefManager == null) {
                sharedPrefManager = mCtx?.let { SharedPrefManager(it) }
            }
            return sharedPrefManager as SharedPrefManager
        }
    }

}


