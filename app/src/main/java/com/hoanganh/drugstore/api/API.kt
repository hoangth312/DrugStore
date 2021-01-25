package com.hoanganh.drugstore.api

import com.hoanganh.drugstore.model.*
import com.hoanganh.drugstore.model.clinic.ClinicModel
import com.hoanganh.drugstore.model.datasearchdrug.SearchDrugsModel
import com.hoanganh.drugstore.model.drugstore.DrugStoreModel
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
                            @Query("name") name: String
    ): Call<List<SearchDrugsModel>>

    @PUT("user/update/password")
    fun putChangePass(@Header("Authorization") auth: String,
                      @Body changePassUser: ChangePassUser): Call<Void>

    @GET("drugstores")
    fun getDrugstore(@Header("Authorization") auth: String): Call<DrugStore>

    @POST("auth/password/reset")
    fun forgotPassword(@Query("email") email: String): Call<Void>

    @GET("drugstores/city")
    fun getAllDrugStoreInCity(@Header("Authorization") auth: String,
                              @Query("city") city: String): Call<List<DrugStoreModel>>

    @GET("clinic/city")
    fun getAllClinicInCity(@Header("Authorization") auth: String,
                           @Query("city") city: String): Call<List<ClinicModel>>


    @GET("products/search/qrcode")
    fun getDrugbyCode(@Header("Authorization") auth: String,
                           @Query("qrCode") city: String):  Call<SearchDrugsModel>


}