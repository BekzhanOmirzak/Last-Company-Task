package com.example.taskfromcompany.ui

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.model.PersonalInformation
import com.example.taskfromcompany.util.Resource
import com.example.taskfromcompany.viewmodel.InformationViewModel
import org.w3c.dom.Text
import kotlin.reflect.full.memberProperties


class PersonalInfFragment : Fragment(R.layout.personal_information) {

    private lateinit var progressBar: ProgressBar
    private lateinit var linearContainer: LinearLayout
    private lateinit var showLinearLayout: NestedScrollView
    private lateinit var personalViewModel: InformationViewModel
    private lateinit var myOnBackPressed: MyOnBackPressed
    private lateinit var tool_bar: Toolbar
    private val TAG = "PersonalInfFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProperties(view)
        initToolBar()
        initObservingPersonalInformation()
        initBackButton()
        if (savedInstanceState == null) personalViewModel.startRequesting()
    }

    private fun initObservingPersonalInformation() {
        personalViewModel.returnPersonalInformation().observe(this) {

            when (it) {
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    showLinearLayout.visibility = View.VISIBLE
                    for (k in PersonalInformation::class.memberProperties) {
                        val text = TextView(requireActivity())
                        text.setLayoutParams(
                            LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        )

                        var value = k.get(it.data!!)

                        if (k.name.lowercase().equals("last_four")) {
                            val str = value.toString()
                            value = str.substring(str.length - 4, str.length)
                        }

                        val property =
                            k.name.substring(0, 1).uppercase() + k.name.substring(1)
                                .lowercase() + ":" + value
                        val ss = SpannableString(property)
                        val foreBold =
                            ForegroundColorSpan(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.bright_black,
                                    null
                                )
                            )

                        ss.setSpan(foreBold, 0, k.name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        text.text = ss
                        text.textSize = 20.0f
                        linearContainer.addView(text)
                    }
                }
                is Resource.Error -> {
                    val text = TextView(activity)
                    text.text = it.message
                    text.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                    linearContainer.addView(text)
                    progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun initProperties(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        linearContainer = view.findViewById(R.id.parent_linear)
        showLinearLayout = view.findViewById(R.id.scroll_view)
        personalViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
        progressBar.visibility = View.VISIBLE
        showLinearLayout.visibility = View.GONE
        tool_bar = view.findViewById(R.id.tool_bar)
        tool_bar.title = ""

        Log.i(TAG, "initProperties PersonalFragment: ${personalViewModel.hashCode()}")

    }

    private fun initToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(tool_bar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
    }

    private fun initBackButton() {

        tool_bar.setNavigationOnClickListener {
            myOnBackPressed.myOnBackPressed(false)
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, OptionsFragment()).commit()

        }

        try {
            myOnBackPressed = activity as MyOnBackPressed
            myOnBackPressed.myOnBackPressed(true)
        } catch (ex: ClassCastException) {
            Toast.makeText(requireActivity(), "Exception has occured...", Toast.LENGTH_LONG).show()
        }

    }

    interface MyOnBackPressed {
        fun myOnBackPressed(pressed: Boolean)
    }


}