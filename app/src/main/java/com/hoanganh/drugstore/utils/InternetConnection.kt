package com.hoanganh.drugstore.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.NonNull

class InternetConnection {

    companion object{
        fun checkConnection(@NonNull context: Context): Boolean {
            return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
        }
    }

}