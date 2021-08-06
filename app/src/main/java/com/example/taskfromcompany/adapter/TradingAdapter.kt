package com.example.taskfromcompany.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskfromcompany.R
import com.example.taskfromcompany.model.CurrencyTrading
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class TradingAdapter : RecyclerView.Adapter<TradingAdapter.TradingViewHolder>() {

    private val tradingItems = mutableListOf<CurrencyTrading>()

    fun updateTradingItems(list: List<CurrencyTrading>) {
        tradingItems.clear()
        tradingItems.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trading_item, parent, false)
        return TradingViewHolder(view);
    }

    override fun onBindViewHolder(holder: TradingViewHolder, position: Int) {

        holder.txtComment.setText(tradingItems[position].Comment)
        holder.txtPair.setText(tradingItems[position].Pair)
        val df = DecimalFormat("#.##")
        val str = df.format(tradingItems[position].Price)
        holder.txtPrice.setText(str)
        holder.txtSl.setText(df.format(tradingItems[position].Sl))
        holder.txtTp.setText(df.format(tradingItems[position].Tp))
        val date = Date(tradingItems[position].ActualTime * 1000L)
        holder.txtActualTime.setText((SimpleDateFormat("dd:MM:yyyy hh:mm").format(date)))
    }

    override fun getItemCount(): Int {
        return tradingItems.size
    }


    inner class TradingViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtActualTime = v.findViewById<TextView>(R.id.txtActualTime)
        val txtComment = v.findViewById<TextView>(R.id.txtComment)
        val txtPair = v.findViewById<TextView>(R.id.txtPair)
        val txtPrice = v.findViewById<TextView>(R.id.txtPrice)
        val txtTp = v.findViewById<TextView>(R.id.txtTp)
        val txtSl = v.findViewById<TextView>(R.id.txtSl)
    }

}