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

    fun saveUser(user: User) {

        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("email", user.email)
        editor.putString("token", user.token)
        editor.putString("userName", user.userName)
        editor.apply()
    }




       fun getID(): Int { return sharedPreferences.getInt("id",0)}
      fun getEmail(): String? {return sharedPreferences.getString("email","")}
        fun getToken (): String? {return sharedPreferences.getString("token","")}
        fun getUserName(): String?  {return sharedPreferences.getString("userName","")}




    companion object {
        val GET_USER = "getUser"
        val SET_USER = "setUser"
        private var sharedPrefManager: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (sharedPrefManager == null) {
                sharedPrefManager = SharedPrefManager(mCtx)
            }
            return sharedPrefManager as SharedPrefManager
        }
    }

}


