package com.hoanganh.drugstore.api

import com.hoanganh.drugstore.Model.DrugStore
import com.hoanganh.drugstore.Model.LoginResponse
import com.hoanganh.drugstore.Model.User
import retrofit2.Call
import retrofit2.http.*

interface API {


    @POST("auth/login")
    fun userLogin(@Body user: LoginResponse): Call<User>

    @GET("drugstores")
    fun getDrugstore(@Header("Authorization") auth: String): Call<DrugStore>

}