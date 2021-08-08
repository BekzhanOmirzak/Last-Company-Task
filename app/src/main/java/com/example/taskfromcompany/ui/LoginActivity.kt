package com.example.taskfromcompany.ui

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.databinding.ActivityLoginBinding
import com.example.taskfromcompany.remote.ConnectionLiveData
import com.example.taskfromcompany.util.Resource
import com.example.taskfromcompany.util.TempDataStorage
import com.example.taskfromcompany.viewmodel.LoginPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.log


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    @Named("String 2")
    @Inject
    lateinit var str: String

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
                it.putExtra("old", "old")
                startActivity(it)
            }
        }


        binding.appCompatButton.setOnClickListener {
            handleButton()
        }
        initObservingLoginInViewModel()
        initHandlingInternet()
        handlingColorForNotMember()


        Log.i(TAG, "onCreate: provided $str ")

    }

    private fun initObservingLoginInViewModel() {
        loginViewModel.returnLiveDataUserLoginIn().observe(this) {

            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Please, check your email and password...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Intent(this, MenuActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }

        }
    }

    private fun handlingColorForNotMember() {
        val text = "Not a member?Sign Up"
        val ss = SpannableString(text)
        val fcsBlue =
            ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.my_blue, null))
        ss.setSpan(fcsBlue, 13, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.txtSignUp.text = ss
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

        val login = binding.edtLogin.text.toString()
        val password = binding.edtPassword.text.toString()
        if (login.trim().isEmpty()) {
            binding.edtLogin.error = "Login can't be empty"
            binding.edtLogin.requestFocus()
            return
        }
        if (password.trim().isEmpty()) {
            binding.edtPassword.error = "Password can't be empty"
            binding.edtPassword.requestFocus()
            return
        }

        try {
            loginViewModel.getApiTokenRest(Integer.valueOf(login), password)
        } catch (ex: Exception) {
            Toast.makeText(
                this,
                "Please, check your email and password...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}

