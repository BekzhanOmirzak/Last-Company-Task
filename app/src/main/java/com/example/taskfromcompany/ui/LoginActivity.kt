package com.example.taskfromcompany.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.databinding.ActivityLoginBinding
import com.example.taskfromcompany.util.TempDataStorage
import com.example.taskfromcompany.viewmodel.LoginPageViewModel

class LoginActivity : AppCompatActivity() {


    private val TAG = "MainActivity"

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        TempDataStorage.initializeSharedPreferences(this)
        loginViewModel = ViewModelProvider(this).get(LoginPageViewModel::class.java)

        val user = TempDataStorage.getCurUser()
        if (user != null) {
            Intent(this, MenuActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.appCompatButton.setOnClickListener {
            loginViewModel.getApiTokenRest(20234561, "ladevi31")
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

    }


}

