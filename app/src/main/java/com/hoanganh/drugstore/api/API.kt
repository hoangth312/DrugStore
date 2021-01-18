package com.hoanganh.drugstore.api

import com.hoanganh.drugstore.Model.LoginResponse
import com.hoanganh.drugstore.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.POST

interface API {


    @POST("auth/login")
  fun userLogin(@Body user : LoginResponse): Call<User>



}