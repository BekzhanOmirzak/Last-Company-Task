package com.example.taskfromcompany.di

import com.example.taskfromcompany.remote.RetrofitApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.internal.Util
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {


    @Singleton
    @Provides
    fun provideOkkHttpClient() = OkHttpClient.Builder()
        .protocols(Util.immutableList(Protocol.HTTP_1_1))
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()


    @Singleton
    @Provides
    @Named("ret_url")
    fun provideRetrofitInstance(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://client-api.contentdatapro.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build();


    @Singleton
    @Provides
    @Named("ret_api")
    fun provideRetrofitRestInstance(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://peanut.ifxdb.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()


    @Singleton
    @Provides
    @Named("rest_api")
    fun provideRetrofitApi(@Named("ret_api") retrofit: Retrofit) =
        retrofit.create(RetrofitApi::class.java)


    @Singleton
    @Provides
    @Named("url_api")
    fun provideRetrofitUrl(@Named("ret_url") retrofit: Retrofit) =
        retrofit.create(RetrofitApi::class.java)


}