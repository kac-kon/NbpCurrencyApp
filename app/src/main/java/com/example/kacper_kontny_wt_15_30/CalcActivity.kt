package com.example.kacper_kontny_wt_15_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.DecimalFormat

class CalcActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    internal lateinit var spinner: Spinner
    internal lateinit var switch: Switch
    internal lateinit var button: Button
    internal lateinit var selectedCurrencyTextView: TextView
    internal lateinit var currentCalcTextView: TextView
    internal lateinit var number: EditText
    internal lateinit var answer: TextView
    internal lateinit var currencyTo: TextView
    var toPLN = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc)

        button = findViewById(R.id.calculateButton)
        spinner = findViewById(R.id.currencySelectSpinner)
        switch = findViewById(R.id.calcSwitch)
        switch.setOnCheckedChangeListener { buttonView, isChecked -> stateChanged(buttonView, isChecked) }
        number = findViewById(R.id.valueTextNumber)
        answer = findViewById(R.id.answerTextView)
        currencyTo = findViewById(R.id.currencyTo)

        selectedCurrencyTextView = findViewById(R.id.selectedCurrency)
        currentCalcTextView = findViewById(R.id.currentCalcSwitch)
        button.setOnClickListener { calculate() }
        var spinnerItems = emptyArray<String>()
        for (element in CurrenciesSingleton.getData()){
            spinnerItems += element.currencyCode
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        print(spinnerItems.size)

    }

    private fun stateChanged(buttonView: CompoundButton?, checked: Boolean) {
        toPLN = !checked
        textUpdate()
        calculate()
    }

    fun textUpdate(){
        if (switch.isChecked){
            currentCalcTextView.text = getString(R.string.currentCalcSwitch).format("PLN", spinner.selectedItem.toString())
            selectedCurrencyTextView.text = "PLN"
            currencyTo.text = spinner.selectedItem.toString()
        } else {
            currentCalcTextView.text = getString(R.string.currentCalcSwitch).format(spinner.selectedItem.toString(), "PLN")
            selectedCurrencyTextView.text = spinner.selectedItem.toString()
            currencyTo.text = "PLN"
        }
    }

    fun calculate(){
        val currency = CurrenciesSingleton.getData()[spinner.selectedItemPosition]
        val ans = when {
            number.text.toString() == "" -> {
                0.0
            }
            toPLN -> {
                currency.currentRate / currency.multiplier * number.text.toString().toDouble()
            }
            else -> {
                1 / (currency.currentRate / currency.multiplier) * number.text.toString().toDouble()
            }
        }
        val df = DecimalFormat("0.####")
        answer.text = df.format(ans)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        textUpdate()
        calculate()
        println(parent?.getItemAtPosition(position).toString() + "selected")
    }


}