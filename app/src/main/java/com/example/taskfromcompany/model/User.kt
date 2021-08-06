package com.example.taskfromcompany.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(var login: Int, var password: String) {

     @SerializedName("token")
     @Expose
     var restToken: String = ""
     var urlToken: String = ""

}