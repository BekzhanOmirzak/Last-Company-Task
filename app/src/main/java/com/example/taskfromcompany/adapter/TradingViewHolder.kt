package com.example.taskfromcompany.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskfromcompany.R

class TradingViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val txtActualTime = v.findViewById<TextView>(R.id.txtActualTime)
    val txtComment = v.findViewById<TextView>(R.id.txtComment)
    val txtPair = v.findViewById<TextView>(R.id.txtPair)
    val txtPrice = v.findViewById<TextView>(R.id.txtPrice)
    val txtTp = v.findViewById<TextView>(R.id.txtTp)
    val txtSl = v.findViewById<TextView>(R.id.txtSl)
}
