package com.example.taskfromcompany.ui

import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskfromcompany.R
import com.example.taskfromcompany.viewmodel.InformationViewModel

class OptionsFragment : Fragment() {

    private lateinit var btn_personal: AppCompatButton
    private lateinit var btn_trading: AppCompatButton
    private lateinit var informationViewModel: InformationViewModel
    private lateinit var passFragment: PassFragment
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private val TAG = "OptionsFragment"


    interface PassFragment {
        fun onPassFragment(fragment: Fragment)
    }

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
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
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
            passFragment(PersonalInfFragment())
            showNextFragment(PersonalInfFragment())
        }

        btn_trading.setOnClickListener {
            passFragment(TradingFragment())
            showNextFragment(TradingFragment())
        }

    }

    private fun showNextFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit)
        transaction.replace(R.id.container, fragment).commit()
    }


    private fun passFragment(fragment: Fragment) {
        try {
            passFragment = activity as PassFragment
            passFragment.onPassFragment(fragment)
        } catch (ex: ClassCastException) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out) {
            Toast.makeText(requireActivity(), "Log out has been selected", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }





}