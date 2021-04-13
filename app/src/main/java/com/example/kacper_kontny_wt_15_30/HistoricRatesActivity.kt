package com.example.kacper_kontny_wt_15_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class HistoricRatesActivity : AppCompatActivity() {
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var chart1: LineChart
    internal lateinit var chart2: LineChart
    internal lateinit var currency: CurrencyDetails
    internal lateinit var historicRates: Array<Pair<String, Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_rates)

        todayRate = findViewById(R.id.history_today)
        yesterdayRate = findViewById(R.id.history_yesterday)
        chart1 = findViewById(R.id.history_chart1)
        chart2 = findViewById(R.id.history_chart2)

        val pos = intent.getIntExtra("position", 0)
        currency = CurrenciesSingleton.getData()[pos]
        println(currency.currencyCode)
        println(currency.table)
        getData()
    }

    private fun getData() {
        val queue = CurrenciesSingleton.getQueue()
        val url = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30/?format=JSON".format(currency.table, currency.currencyCode)

        val historicRatesRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                loadDetails(response)
                setDetails()
            },
            Response.ErrorListener { TODO("ERROR no internet connection") }
        )
        queue.add(historicRatesRequest)
    }

    private fun setDetails() {
        todayRate.text = getString(R.string.history_today_text, historicRates.last().second)
        yesterdayRate.text = getString(R.string.history_yesterday_text, historicRates[historicRates.size-2].second)

        var entries = ArrayList<Entry>()
        for ((index, element) in historicRates.withIndex()){
            entries.add(Entry(index.toFloat(), element.second.toFloat()))
        }
        val lineData7 = LineData(LineDataSet(entries.subList(entries.size-7, entries.size), "Kurs"))
        val lineData30 = LineData(LineDataSet(entries, "Kurs"))
        chart1.data = lineData7
        chart1.xAxis.valueFormatter = IndexAxisValueFormatter(historicRates.
        sliceArray(historicRates.lastIndex-7..historicRates.lastIndex)
            .map { it.first }.toTypedArray())
        chart1.axisRight.setDrawLabels(false)
        chart1.description.text = "last 7 rates"
        chart1.invalidate()

        chart2.data = lineData30
        chart2.xAxis.valueFormatter = IndexAxisValueFormatter(historicRates.map { it.first }.toTypedArray())
        chart2.axisRight.setDrawLabels(false)
        chart2.description.text = "last 30 rates"
        chart2.invalidate()

    }

    private fun loadDetails(response: JSONObject?) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val tmpData = arrayOfNulls<Pair<String, Double>>(rates.length())
            for (i in 0 until rates.length()){
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                tmpData[i] = Pair(date, currencyRate)
            }

            this.historicRates = tmpData as Array<Pair<String, Double>>
        }
    }


}