package com.example.taskfromcompany.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskfromcompany.R
import com.example.taskfromcompany.adapter.TradingAdapter
import com.example.taskfromcompany.util.Resource
import com.example.taskfromcompany.viewmodel.InformationViewModel
import com.google.android.material.slider.RangeSlider
import java.lang.ClassCastException
import java.text.SimpleDateFormat
import java.util.*

class TradingFragment : Fragment(R.layout.currency_trading) {

    private val TAG = "TradingFragment"

    private lateinit var informationViewModel: InformationViewModel

    private lateinit var rec_view: RecyclerView
    private lateinit var btnSearch: Button
    private lateinit var adapter: TradingAdapter
    private lateinit var rangeSeek: RangeSlider
    private lateinit var txtStartDate: TextView
    private lateinit var txtFinishData: TextView
    private lateinit var spinner: Spinner
    private lateinit var toolBar: Toolbar
    private lateinit var myOnBackPressed: PersonalInfFragment.MyOnBackPressed
    private lateinit var progressBar: ProgressBar
    private lateinit var txtNoResult: TextView
    private var from = 0
    private var to = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProperties(view)
        initToolBar()
        val cur_time = "${System.currentTimeMillis()}".substring(0, 10)
        rangeSeek.valueTo = Integer.valueOf(cur_time) * 1.0f
        initRecyclerView()

        rangeSeek.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {

            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                changeDateValueForText(slider.values.get(0).toInt(), slider.values.get(1).toInt())
            }

        })
        btnSearch.setOnClickListener {
            startSearchingCurrencyTrading()
        }
        initHandlingBackButton()

    }

    private fun initToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
    }

    private fun initProperties(view: View) {
        btnSearch = view.findViewById(R.id.btnSearch)
        rec_view = view.findViewById(R.id.rec_view)
        rangeSeek = view.findViewById(R.id.rangeSlider)
        txtStartDate = view.findViewById(R.id.txtFrom)
        txtFinishData = view.findViewById(R.id.txtTo)
        spinner = view.findViewById(R.id.spinner)
        toolBar = view.findViewById(R.id.tool_bar)
        progressBar = view.findViewById(R.id.progress_bar)
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
        txtNoResult = view.findViewById(R.id.txtNoResults)
        toolBar.title = ""
        rec_view.visibility = View.GONE
        from = rangeSeek.values.get(0).toInt()
        to = rangeSeek.values.get(1).toInt()
    }

    private fun initHandlingBackButton() {

        try {
            myOnBackPressed = activity as PersonalInfFragment.MyOnBackPressed
            myOnBackPressed.myOnBackPressed(true)
        } catch (ex: ClassCastException) {

        }

        toolBar.setNavigationOnClickListener {
            myOnBackPressed.myOnBackPressed(false)
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, OptionsFragment()).commit()
        }


    }

    private fun startSearchingCurrencyTrading() {

        if (!(activity as MenuActivity).hasInternet) {
            Toast.makeText(
                activity,
                "Please, check your internet connection...",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val pair = spinner.selectedItem.toString()
        informationViewModel.startRequestingCurrency(pair, from, to)
    }

    private fun changeDateValueForText(start: Int, finish: Int) {
        from = start
        to = finish
        val startDate = Date(start * 1000L)
        val finishDate = Date(finish * 1000L)
        val df = SimpleDateFormat("dd:MM:yyyy")
        txtStartDate.text = df.format(startDate)
        txtFinishData.text = df.format(finishDate)
    }

    private fun initRecyclerView() {
        adapter = TradingAdapter()
        rec_view.adapter = adapter
        rec_view.layoutManager = LinearLayoutManager(requireActivity())
        informationViewModel.returnCurrentTrading().observe(this) {

            when (it) {
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    txtNoResult.setText(it.message)
                    txtNoResult.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    rec_view.visibility = View.GONE
                    txtNoResult.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    if (it.data?.size == 0) {
                        txtNoResult.visibility = View.VISIBLE
                    } else {
                        rec_view.visibility = View.VISIBLE
                        txtNoResult.visibility = View.GONE
                        adapter.updateTradingItems(it.data!!)
                    }
                }
            }

        }
    }


}