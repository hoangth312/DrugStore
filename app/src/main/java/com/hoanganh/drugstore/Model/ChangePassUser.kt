package com.hoanganh.drugstore.Model

import com.google.gson.annotations.SerializedName

data class ChangePassUser(




    @SerializedName("username")
    val userName: String,
    val oldPassword: String,
    @SerializedName("password")
    val passWord: String,
    val confirmPassword: String
)