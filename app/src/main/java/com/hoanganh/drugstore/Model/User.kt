package com.hoanganh.drugstore.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    val email: String,
    val id: Int,
    val roles: List<String>,
    val token: String,
    val type: String,
    @SerializedName("username")
    @Expose
    val userName: String
) {

}