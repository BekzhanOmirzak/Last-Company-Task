package com.example.taskfromcompany.ui

import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.util.TempDataStorage
import com.example.taskfromcompany.viewmodel.InformationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OptionsFragment : Fragment() {

    private lateinit var btn_personal: AppCompatButton
    private lateinit var btn_trading: AppCompatButton
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private val TAG = "OptionsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.options_fragment, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_personal = view.findViewById(R.id.btn_personal_inf)
        btn_trading = view.findViewById(R.id.btn_trading)
        toolBar = view.findViewById(R.id.tool_bar)
        toolBar.title = ""
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            toolBar.overflowIcon?.setColorFilter(
                BlendModeColorFilter(
                    ContextCompat.getColor(
                        (requireContext()),
                        R.color.white
                    ), BlendMode.SRC_ATOP
                )
            )
        } else {
            toolBar.overflowIcon?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                ), PorterDuff.Mode.SRC_ATOP
            )
        }


        btn_personal.setOnClickListener {
            showNextFragment(PersonalInfFragment())
        }

        btn_trading.setOnClickListener {
            showNextFragment(TradingFragment())
        }


    }

    private fun showNextFragment(fragment: Fragment) {

        if (!(activity as MenuActivity).hasInternet) {
            Toast.makeText(
                activity,
                "Please, check your internet connection...",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit)
        transaction.replace(R.id.container, fragment).commit()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out) {
            TempDataStorage.saveUser(null)
            Intent(requireActivity(), LoginActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(it)
            }
            return true
        }
        return false
    }


}