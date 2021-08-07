package com.example.taskfromcompany.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.databinding.ActivityLoginBinding
import com.example.taskfromcompany.remote.ConnectionLiveData
import com.example.taskfromcompany.util.TempDataStorage
import com.example.taskfromcompany.viewmodel.LoginPageViewModel

class LoginActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginPageViewModel
    private lateinit var connectionLiveData: ConnectionLiveData
    private var hasInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionLiveData = ConnectionLiveData(this)
        TempDataStorage.initializeSharedPreferences(this)
        loginViewModel = ViewModelProvider(this).get(LoginPageViewModel::class.java)


        val user = TempDataStorage.getCurUser()
        if (user != null) {
            Intent(this, MenuActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.appCompatButton.setOnClickListener {
            handleButton()
        }

        loginViewModel.returnLiveDataUserLoginIn().observe(this) {
            if (it) {
                Intent(this, MenuActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Log.e(TAG, "onCreate: Error")
            }
        }
        initHandlingInternet()

    }

    private fun initHandlingInternet() {
        connectionLiveData.observe(this) {
            hasInternet = it
            if (it) {
                binding.txtWarning.visibility = View.GONE
            } else {
                binding.txtWarning.visibility = View.VISIBLE
            }
        }
    }

    private fun handleButton() {
        if (!hasInternet) {
            Toast.makeText(this, "Please, check your internet connection..", Toast.LENGTH_SHORT)
                .show()
            return
        }
        loginViewModel.getApiTokenRest(20234561, "ladevi31")
    }


}

