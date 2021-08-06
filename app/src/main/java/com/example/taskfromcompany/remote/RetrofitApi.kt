package com.example.taskfromcompany.remote

import com.example.taskfromcompany.model.CurrencyTrading
import com.example.taskfromcompany.model.LoginResponse
import com.example.taskfromcompany.model.PersonalInformation
import com.example.taskfromcompany.model.User
import com.example.taskfromcompany.util.TempDataStorage
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {


    @POST("api/ClientCabinetBasic/IsAccountCredentialsCorrect")
    suspend fun askForApiTokenRest(
        @Body user: User
    ): Response<LoginResponse>


    @POST("api/Authentication/RequestMoblieCabinetApiToken")
    suspend fun askForApiTokenUrl(
        @Body user: User
    ): Response<String>

    @POST("api/ClientCabinetBasic/GetAccountInformation")
    suspend fun getPersonalInformation(@Body user: User): Response<PersonalInformation>

    @POST("api/ClientCabinetBasic/GetLastFourNumbersPhone")
    suspend fun getLastFourNumber(@Body user: User): Response<String>

    @GET("clientmobile/GetAnalyticSignals/{login}")
    suspend fun getCurrentTrading(
        @Header("passkey") passkey: String,
        @Path("login") login: Int,
        @Query("tradingsystem") system: Int,
        @Query("pairs") pairs: String,
        @Query("from") from: Int,
        @Query("to") to: Int
    ): Response<List<CurrencyTrading>>




}