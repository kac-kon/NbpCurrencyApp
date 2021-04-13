// Kacper Kontny gr 4b wt 15:30
//
// wykonane wszystkie ćwiczenia
//
// flagi walut pobrane z:
// https://github.com/transferwise/currency-flags/tree/master/src/flags
package com.example.kacper_kontny_wt_15_30

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest

class MainActivity : AppCompatActivity() {
    var dataLoaded: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CurrenciesSingleton.prepareSingleton(applicationContext)
        makeRequest()


    }

    private fun makeRequest() {
        val queue = CurrenciesSingleton.getQueue()
        val currenciesRatesARequest = getCurrencyRequest(getString(R.string.url_a))
        val currenciesRatesBRequest = getCurrencyRequest(getString(R.string.url_b), false)
        val goldRatesRequest = getGoldRequest(getString(R.string.url_g))

        queue.add(currenciesRatesARequest)
        queue.add(currenciesRatesBRequest)
        queue.add(goldRatesRequest)
    }

    private fun getGoldRequest(url: String): JsonArrayRequest {
        val goldRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                CurrenciesSingleton.loadGoldData(response)
            },
            Response.ErrorListener { error ->
                noConnectionToast(error.toString())
            }
        )
        return goldRequest
    }

    fun getCurrencyRequest(url: String, new: Boolean = true): JsonArrayRequest{
        return JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                println("Success")
                CurrenciesSingleton.loadData(response, new)
            },
            Response.ErrorListener { error ->
                noConnectionToast(error.toString())
            }
        )
    }

    private fun noConnectionToast(error: String = "no Internet connection, wait or restart the application and try again") {
        print("no connection")
        val toast = Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun viewCurrencies(view: View) {
        if (CurrenciesSingleton.getData().size > 140) {
            startActivity(Intent(this@MainActivity, CurrencyViewsActivity::class.java))
        } else {
            noConnectionToast()
        }

    }

    fun viewGold(view: View) {
        if (CurrenciesSingleton.getData().size > 140){
            startActivity(Intent(this@MainActivity, GoldActivity::class.java))
        } else {
            noConnectionToast()
        }

    }

    fun viewCalc(view: View) {
        if (CurrenciesSingleton.getData().size > 140){
            startActivity(Intent(this@MainActivity, CalcActivity::class.java))
        } else {
            noConnectionToast()
        }

    }
}