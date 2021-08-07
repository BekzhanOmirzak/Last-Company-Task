package com.example.taskfromcompany.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData


class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private val TAG = "ConnectionLiveData"

    private lateinit var networkCallBack: ConnectivityManager.NetworkCallback
    private val cm =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()


    override fun onActive() {
        networkCallBack = createNetworkCallBack()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallBack)
    }


    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallBack)
    }

    private fun createNetworkCallBack() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {

            val networkCapabilities = cm.getNetworkCapabilities(network)
            val isInternet = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)!!
            if (isInternet) {
                validNetworks.add(network)
            }
            checkValideNetworks()
        }


        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValideNetworks()
        }


    }

    private fun checkValideNetworks() {
        postValue(validNetworks.size > 0)
    }


}