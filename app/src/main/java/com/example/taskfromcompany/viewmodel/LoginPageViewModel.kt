package com.example.taskfromcompany.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskfromcompany.model.User
import com.example.taskfromcompany.remote.RetrofitApi
import com.example.taskfromcompany.util.Resource
import com.example.taskfromcompany.util.TempDataStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginPageViewModel
@Inject constructor(
    @Named("rest_api") val retrofitApiRest: RetrofitApi,
    @Named("url_api") val retrofitApiUrl: RetrofitApi
) : ViewModel() {

    private val TAG = "LoginPageViewModel"

    private val apiTokenRestUrl = MutableLiveData<Resource<Boolean>>();

    fun returnLiveDataUserLoginIn(): LiveData<Resource<Boolean>> {
        return apiTokenRestUrl
    }


    fun getApiTokenRest(login: Int, password: String) {
        apiTokenRestUrl.postValue(Resource.Loading(false))
        val user = User(login, password)
        viewModelScope.launch {

            val callRest = retrofitApiRest.askForApiTokenRest(user)
            val callUrl = retrofitApiUrl.askForApiTokenUrl(user)

            if (callRest.isSuccessful && callUrl.isSuccessful && callRest.code() == 200 && callUrl.code() == 200) {
                withContext(Dispatchers.Main) {
                    apiTokenRestUrl.postValue(Resource.Success(true))
                    user.restToken = callRest.body()!!.token
                    user.urlToken = callUrl.body()!!
                    TempDataStorage.saveUser(user)
                }
            } else if (!callRest.isSuccessful && !callUrl.isSuccessful) {
                withContext(Dispatchers.Main) {
                    apiTokenRestUrl.postValue(Resource.Error("Error"))
                }

            } else if (!callRest.isSuccessful || !callUrl.isSuccessful) {
                withContext(Dispatchers.Main) {
                    apiTokenRestUrl.postValue(Resource.Loading(false))
                }

            }


        }


    }



}