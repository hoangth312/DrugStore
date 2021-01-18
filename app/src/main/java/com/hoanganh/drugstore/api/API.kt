package com.hoanganh.drugstore.api

import com.hoanganh.drugstore.Model.DrugStore
import com.hoanganh.drugstore.Model.LoginResponse
import com.hoanganh.drugstore.Model.RegisterAccount
import com.hoanganh.drugstore.Model.User
import com.hoanganh.drugstore.Model.datasearchdrug.SearchDrugsModel
import retrofit2.Call
import retrofit2.http.*

interface API {

    @POST("auth/signup")
    fun signUp(@Body user: RegisterAccount): Call<RegisterAccount>


    @POST("auth/login")
    fun userLogin(@Body user: LoginResponse): Call<User>

    @POST("auth/logout")
    fun userLogout(): Call<User>


    @GET("products/search/name")
    fun getDrugSearchbyName(@Header("Authorization") auth: String,
                            @Query("language") language: String,
                            @Query("productName") productName: String

    ): Call<List<SearchDrugsModel>>




    @GET("drugstores")
    fun getDrugstore(@Header("Authorization") auth: String): Call<DrugStore>

}