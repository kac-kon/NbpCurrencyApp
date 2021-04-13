package com.example.kacper_kontny_wt_15_30

data class CurrencyDetails(var currencyCode: String,
                           var currentRate: Double,
                           var previousRate: Double = 0.0,
                           var table: String,
                           var multiplier: Int = 1)