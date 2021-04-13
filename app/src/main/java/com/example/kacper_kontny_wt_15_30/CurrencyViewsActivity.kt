package com.example.kacper_kontny_wt_15_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray

class CurrencyViewsActivity : AppCompatActivity() {
    internal lateinit var currencyRecycler: RecyclerView
    internal lateinit var adapter: CurrenciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_views)

        currencyRecycler = findViewById(R.id.currency_recyclerID)
        currencyRecycler.layoutManager = LinearLayoutManager(this)
        adapter = CurrenciesAdapter(CurrenciesSingleton.getData(), this)
        currencyRecycler.adapter = adapter

        adapter.dataSet = CurrenciesSingleton.getData()
    }

}