package com.example.taskfromcompany.util

import android.content.Context
import android.content.SharedPreferences
import com.example.taskfromcompany.model.LoginResponse
import com.example.taskfromcompany.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TempDataStorage {

    private lateinit var share: SharedPreferences;
    private val gson = Gson();
    private val type = object : TypeToken<User>() {}.type

    fun initializeSharedPreferences(context: Context) {
        share = context.getSharedPreferences("fake_db", Context.MODE_PRIVATE)
    }

    fun saveUser(user: User) = share.edit().putString("user", gson.toJson(user)).apply()

    fun getCurUser(): User? {
        val user_str = share.getString("user", null) ?: return null
        val user = gson.fromJson<User>(user_str, type)
        return user
    }




}