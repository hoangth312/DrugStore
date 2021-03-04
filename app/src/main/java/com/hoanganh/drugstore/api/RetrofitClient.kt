package com.hoanganh.drugstore.api





import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


object RetrofitClient {



//private const val BASE_URL = "http://10.99.11.3:2020/api/"
//private const val BASE_URL = "http://192.168.43.29:2020/api/"

// private const val BASE_URL = "http://10.99.20.26:2020/api/"
//    private const val BASE_URL = "http://172.16.101.177:2020/api/"

    private const val BASE_URL = "http://192.168.1.130:2020/api/"

    var gson: Gson = GsonBuilder()
            .setLenient()
            .create()

    private val okHttpClient = OkHttpClient.Builder()

            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .protocols(listOf(Protocol.HTTP_1_1))


            .build()



    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }



    fun getApiService(): API {
        return getRetrofitInstance().create(API::class.java)
    }



}

