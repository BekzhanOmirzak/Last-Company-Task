package com.example.taskfromcompany.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.databinding.ActivityMenuBinding
import com.example.taskfromcompany.remote.ConnectionLiveData
import com.example.taskfromcompany.util.Resource
import com.example.taskfromcompany.util.TempDataStorage
import com.example.taskfromcompany.viewmodel.InformationViewModel
import com.example.taskfromcompany.viewmodel.LoginPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.ConnectException

@AndroidEntryPoint
class MenuActivity : AppCompatActivity(),
    PersonalInfFragment.MyOnBackPressed {

    private val TAG = "Menu"
    private lateinit var binding: ActivityMenuBinding
    private val loginPageViewModel: LoginPageViewModel by viewModels()
    private var pressed = false
    private lateinit var connectLiveData: ConnectionLiveData
    var hasInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        connectLiveData = ConnectionLiveData(this)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            showFragment(OptionsFragment())
        }
        initHandlingInternetConnection()

    }


    private fun initHandlingInternetConnection() {

        connectLiveData.observe(this) {
            hasInternet = it

            if (it) {
                binding.txtWarning.visibility = View.GONE
            } else {
                binding.txtWarning.visibility = View.VISIBLE
            }
        }

    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (pressed) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
            transaction.replace(R.id.container, OptionsFragment())
                .commit()
            pressed = false
        } else {
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    override fun myOnBackPressed(pressed: Boolean) {
        this.pressed = pressed
    }


}