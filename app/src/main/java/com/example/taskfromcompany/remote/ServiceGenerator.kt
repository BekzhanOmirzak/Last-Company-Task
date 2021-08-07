package com.example.taskfromcompany.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.internal.Util
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ServiceGenerator {

    private val client = OkHttpClient.Builder()
        .protocols(Util.immutableList(Protocol.HTTP_1_1))
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private var retrofitREST = Retrofit.Builder()
        .baseUrl("https://peanut.ifxdb.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    fun generateServiceRest(): RetrofitApi {
        return retrofitREST.create(RetrofitApi::class.java)
    }

    private var retrofitURL = Retrofit.Builder()
        .baseUrl("https://client-api.contentdatapro.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    fun generateServiceUrl(): RetrofitApi {
        return retrofitURL.create(RetrofitApi::class.java)
    }


}