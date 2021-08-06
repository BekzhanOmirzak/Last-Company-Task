package com.example.taskfromcompany.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskfromcompany.model.CurrencyTrading
import com.example.taskfromcompany.model.PersonalInformation
import com.example.taskfromcompany.remote.ServiceGenerator
import com.example.taskfromcompany.util.TempDataStorage
import kotlinx.coroutines.*

class InformationViewModel : ViewModel() {

    private val TAG = "InformationViewModel"

    val retrofitApiRest = ServiceGenerator.generateServiceRest()
    val retrofitApiUrl = ServiceGenerator.generateServiceUrl()
    private val liveDataUserInformation = MutableLiveData<PersonalInformation?>()
    private val liveDataListCurrencyTranding = MutableLiveData<List<CurrencyTrading>?>()
    private var job: Job? = null
     var fragment: Fragment? = null


    fun returnPersonalInformation(): LiveData<PersonalInformation?> {
        return liveDataUserInformation
    }

    fun returnCurrentTrading(): LiveData<List<CurrencyTrading>?> {
        return liveDataListCurrencyTranding
    }

    fun startRequesting() {
        liveDataUserInformation.postValue(null)
        val user = TempDataStorage.getCurUser()!!
        viewModelScope.launch {

            val callPersonal = retrofitApiRest.getPersonalInformation(user)
            val callLastFour = retrofitApiRest.getLastFourNumber(user)

            if (callPersonal.isSuccessful && callLastFour.isSuccessful && callPersonal.code() == 200 && callLastFour.code() == 200) {
                val personalInformation = callPersonal.body()!!
                personalInformation.last_four = callLastFour.body()!!
                withContext(Dispatchers.Main) {
                    liveDataUserInformation.postValue(personalInformation)
                }
            } else {
                withContext(Dispatchers.Main) {
                    liveDataUserInformation.postValue(null)
                }
            }
        }

    }

    fun startRequestingCurrency(pair: String, from: Int, to: Int) {
        Log.i(TAG, "startRequestingCurrency: $pair   $from   $to ")
        Log.i(TAG, "startRequestingCurrency: ${TempDataStorage.getCurUser()!!.urlToken}")
        liveDataListCurrencyTranding.postValue(null)
        val user = TempDataStorage.getCurUser()!!
        job = viewModelScope.launch {

            val responseUrl =
                retrofitApiUrl.getCurrentTrading(user.urlToken, user.login, 3, pair, from, to)
            if (responseUrl.isSuccessful && responseUrl.code() == 200) {
                withContext(Dispatchers.Main) {
                    liveDataListCurrencyTranding.postValue(responseUrl.body())
                    Log.i(TAG, "startRequestingCurrency: ${responseUrl.body()}")
                    job?.cancel();
                }
            } else {
                withContext(Dispatchers.Main) {
                    liveDataListCurrencyTranding.postValue(null)
                    job?.cancel()
                }
            }

        }
    }


}