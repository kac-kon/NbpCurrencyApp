package com.example.kacper_kontny_wt_15_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.DecimalFormat

class GoldActivity : AppCompatActivity() {
    internal lateinit var todayGoldRate: TextView
    internal lateinit var chartGold: LineChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)

        todayGoldRate = findViewById(R.id.gold_text)
        chartGold = findViewById(R.id.gold_chart)
        setDetails()
    }

    private fun setDetails() {
        val df = DecimalFormat("0.####")
        todayGoldRate.text = getString(R.string.gold_rate, df.format(CurrenciesSingleton.goldRates.last().second))

        val entries = ArrayList<Entry>()
        for ((index, element) in CurrenciesSingleton.goldRates.withIndex()){
            entries.add(Entry(index.toFloat(), element.second.toFloat()))
        }
        val lineData = LineData(LineDataSet(entries, "Kurs złota"))

        chartGold.data = lineData
        chartGold.xAxis.valueFormatter = IndexAxisValueFormatter(CurrenciesSingleton.goldRates.map { it.first }.toTypedArray())
        chartGold.axisRight.setDrawLabels(false)
        chartGold.description.text = "last 30 rates"
        chartGold.invalidate()
    }
}