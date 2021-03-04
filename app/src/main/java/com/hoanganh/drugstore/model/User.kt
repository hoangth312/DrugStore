package com.hoanganh.drugstore.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    val email: String,
    val id: Int,
    val token: String,
    val type: String,
    @SerializedName("username")
    @Expose
    val userName: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
) {

}