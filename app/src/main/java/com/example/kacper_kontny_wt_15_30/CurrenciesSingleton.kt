package com.example.kacper_kontny_wt_15_30

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray

object CurrenciesSingleton {
    private lateinit var queue: RequestQueue
    internal lateinit var goldRates: Array<Pair<String, Double>>
    private var data: Array<CurrencyDetails> = emptyArray()

    fun prepareSingleton(context: Context){
        queue = newRequestQueue(context)
    }

    fun getQueue(): RequestQueue{
        return queue
    }

    fun loadData(response: JSONArray?, new: Boolean = true){
        response?.let {
            val currentRates = response.getJSONObject(1).getJSONArray("rates")
            var tmpData = arrayOfNulls<CurrencyDetails>(currentRates.length())
            for (i in 0 until currentRates.length()){
                val currencyCode = currentRates.getJSONObject(i).getString("code")
                val currencyRate = currentRates.getJSONObject(i).getDouble("mid")
                val currencyObject = CurrencyDetails(currencyCode, currencyRate,
                    previousRate = response.getJSONObject(0).getJSONArray("rates")
                        .getJSONObject(i).getDouble("mid"),
                    table = response.getJSONObject(0).getString("table"))
                tmpData[i] = currencyObject
            }
            if (new){
                data = tmpData as Array<CurrencyDetails>
            } else {
                data += tmpData as Array<CurrencyDetails>
            }
        }
    }

    fun loadGoldData(response: JSONArray?){
        response?.let{
            val tmpData = arrayOfNulls<Pair<String, Double>>(response.length())
            for (i in 0 until response.length()){
                val date = response.getJSONObject(i).getString("data")
                val currencyRate = response.getJSONObject(i).getDouble("cena")
                tmpData[i] = Pair(date, currencyRate)
            }
            goldRates = tmpData as Array<Pair<String, Double>>
        }
    }

    fun getData(): Array<CurrencyDetails>{
        return data
    }
}