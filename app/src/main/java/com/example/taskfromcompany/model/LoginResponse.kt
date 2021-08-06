package com.example.taskfromcompany.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("result")
    @Expose
    var result: Boolean,
    @SerializedName("token")
    @Expose
    var token: String
)