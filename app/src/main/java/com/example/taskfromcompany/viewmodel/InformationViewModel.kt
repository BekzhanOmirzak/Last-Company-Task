package com.example.taskfromcompany.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskfromcompany.model.CurrencyTrading
import com.example.taskfromcompany.model.PersonalInformation
import com.example.taskfromcompany.remote.RetrofitApi
import com.example.taskfromcompany.remote.ServiceGenerator
import com.example.taskfromcompany.util.Resource
import com.example.taskfromcompany.util.TempDataStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class InformationViewModel
@Inject constructor(
    @Named("rest_api") val retrofitApiRest: RetrofitApi,
    @Named("url_api") val retrofitApiUrl: RetrofitApi
) : ViewModel() {

    private val TAG = "InformationViewModel"

    private val liveDataUserInformation = MutableLiveData<Resource<PersonalInformation>>()
    private val liveDataListCurrencyTrading = MutableLiveData<Resource<List<CurrencyTrading>>>()
    private var job: Job? = null

    fun returnPersonalInformation(): LiveData<Resource<PersonalInformation>> {
        return liveDataUserInformation
    }

    fun returnCurrentTrading(): LiveData<Resource<List<CurrencyTrading>>> {
        return liveDataListCurrencyTrading
    }

    fun startRequesting() {
        liveDataUserInformation.postValue(Resource.Loading(null))
        val user = TempDataStorage.getCurUser()!!
        viewModelScope.launch {

            try {

                val callPersonal = retrofitApiRest.getPersonalInformation(user)
                val callLastFour = retrofitApiRest.getLastFourNumber(user)

                if (callPersonal.isSuccessful && callLastFour.isSuccessful && callPersonal.code() == 200 && callLastFour.code() == 200) {
                    val personalInformation = callPersonal.body()!!
                    personalInformation.last_four = callLastFour.body()!!
                    withContext(Dispatchers.Main) {
                        liveDataUserInformation.postValue(Resource.Success(personalInformation))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        liveDataUserInformation.postValue(Resource.Error("Error "))
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    liveDataUserInformation.postValue(Resource.Error("Network TimeOut"))
                }
            }
        }
    }

    fun startRequestingCurrency(pair: String, from: Int, to: Int) {
        liveDataListCurrencyTrading.postValue(Resource.Loading(null))
        val user = TempDataStorage.getCurUser()!!
        job = viewModelScope.launch {
            try {
                val responseUrl =
                    retrofitApiUrl.getCurrentTrading(user.urlToken, user.login, 3, pair, from, to)
                if (responseUrl.isSuccessful && responseUrl.code() == 200) {
                    withContext(Dispatchers.Main) {
                        liveDataListCurrencyTrading.postValue(Resource.Success(responseUrl.body()!!))
                        job?.cancel();
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        liveDataListCurrencyTrading.postValue(Resource.Error("Error "))
                        job?.cancel()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.IO) {
                    liveDataListCurrencyTrading.postValue(Resource.Error("Network TimeOut "))
                    job?.cancel()
                }
            }
        }
    }

}