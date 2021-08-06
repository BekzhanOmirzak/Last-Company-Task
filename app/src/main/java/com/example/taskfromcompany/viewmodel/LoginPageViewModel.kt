package com.example.taskfromcompany.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskfromcompany.model.User
import com.example.taskfromcompany.remote.ServiceGenerator
import com.example.taskfromcompany.util.TempDataStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class LoginPageViewModel : ViewModel() {

    private val TAG = "LoginPageViewModel"

    val retrofitApiRest = ServiceGenerator.generateServiceRest()
    val retrofitApiUrl = ServiceGenerator.generateServiceUrl();
    private val apiTokenRestUrl = MutableLiveData<Boolean>();


    fun returnLiveDataUserLoginIn(): LiveData<Boolean> {
        return apiTokenRestUrl;
    }


    fun getApiTokenRest(login: Int, password: String) {

        val user = User(login, password)
        viewModelScope.launch {

            val callRest = retrofitApiRest.askForApiTokenRest(user)
            val callUrl = retrofitApiUrl.askForApiTokenUrl(user)

            if (callRest.isSuccessful && callUrl.isSuccessful && callRest.code()==200 && callUrl.code()==200) {
                withContext(Dispatchers.Main) {
                    apiTokenRestUrl.postValue(true)
                    user.restToken=callRest.body()!!.token
                    user.urlToken=callUrl.body()!!
                    TempDataStorage.saveUser(user)
                }
            } else if (!callRest.isSuccessful && !callUrl.isSuccessful) {
                withContext(Dispatchers.Main) {
                    apiTokenRestUrl.postValue(false)
                }
            }
        }




    }


}