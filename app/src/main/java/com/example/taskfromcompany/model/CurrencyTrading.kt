package com.example.taskfromcompany.model

data class CurrencyTrading(
    val ActualTime: Int,
    val Cmd: Int,
    val Comment: String,
    val Id: Int,
    val Pair: String,
    val Period: String,
    val Price: Double,
    val Sl: Double,
    val Tp: Double,
    val TradingSystem: Int
)