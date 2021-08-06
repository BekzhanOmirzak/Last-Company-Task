package com.example.taskfromcompany.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.databinding.ActivityMenuBinding
import com.example.taskfromcompany.viewmodel.InformationViewModel

class MenuActivity : AppCompatActivity(), OptionsFragment.PassFragment,
    PersonalInfFragment.MyOnBackPressed {

    private val TAG = "Menu"
    private lateinit var binding: ActivityMenuBinding
    private lateinit var informationViewModel: InformationViewModel
    private var pressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
        Log.i(TAG, "onCreate: code: ${informationViewModel.hashCode()}")
        if (informationViewModel.fragment != null) {
            showFragment(informationViewModel.fragment!!)
        } else {
            showFragment(OptionsFragment())
        }

    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
            .commit()
    }

    override fun onPassFragment(fragment: Fragment) {
        informationViewModel.fragment = fragment
    }

    override fun onBackPressed() {
        if (pressed) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
            transaction.replace(R.id.container, OptionsFragment())
                .commit()
            pressed = false
        } else {
            super.onBackPressed()
        }
    }

    override fun myOnBackPressed(pressed: Boolean) {
        this.pressed = pressed
    }


}