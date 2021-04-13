package com.example.kacper_kontny_wt_15_30

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.content.res.Resources

fun Context.resIdByName(resIdName: String?, resType: String): Int {
    resIdName?.let {
        return resources.getIdentifier(it, resType, packageName)
    }
    throw Resources.NotFoundException()
}

class CurrenciesAdapter(var dataSet: Array<CurrencyDetails>, private val context: Context) : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView = view.findViewById(R.id.currencyCode)
        val rateTextView: TextView = view.findViewById(R.id.rate)
        val flagView: ImageView = view.findViewById(R.id.flagView)
        var rateImageView: ImageView = view.findViewById(R.id.rateImage)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_currency_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = dataSet[position]
        if (currency.currentRate < 0.1){
            while (currency.currentRate < 1){
                currency.currentRate *= 10
                currency.multiplier *= 10
            }
        }
        holder.currencyCodeTextView.text = (currency.multiplier.toString() + " " + currency.currencyCode)
        holder.rateTextView.text = (currency.currentRate.toString().take(6) + " PLN")
        val drawableResId = if (currency.currencyCode == "TRY"){
            context.resIdByName("try_", "drawable")
        } else{
            context.resIdByName(currency.currencyCode.toLowerCase(), "drawable")
        }
        if (drawableResId == 0) {
            holder.flagView.setImageResource(R.drawable.glb)
        } else {
            holder.flagView.setImageResource(drawableResId)
        }
        if (currency.currentRate / currency.multiplier >= currency.previousRate){
            holder.rateImageView.setImageResource(R.drawable.up)
        } else {
            holder.rateImageView.setImageResource(R.drawable.down)
        }

        holder.itemView.setOnClickListener{ goToDetails(position) }
    }

    private fun goToDetails(position: Int) {
        val intent = Intent(context, HistoricRatesActivity::class.java).apply {
            putExtra("position", position)
        }
        context.startActivity(intent)
    }
}
