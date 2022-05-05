package com.safacet.tradetracker.model.stock

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.Transaction
import com.safacet.tradetracker.utils.SUPPORTED_SYMBOLS
import com.safacet.tradetracker.viewmodel.HomeStockListItem
import org.json.JSONObject
import java.sql.Timestamp
import java.util.*

data class Stock (
    @PropertyName("userEmail") var userEmail: String = ""
        ) {
    @PropertyName("currencyAverage") var currencyAverage: Double = 0.0
    @PropertyName("fromAmountTotal") var fromAmountTotal: Double = 0.0
    @PropertyName("fromUnit") var fromUnit: String = ""
    @PropertyName("systemDate") var systemDate: Date = Date(0)
    @PropertyName("toAmountTotal") var toAmountTotal: Double = 0.0
    @PropertyName("toUnit") var toUnit: String = ""

    fun onFirstBuy(transaction: com.safacet.tradetracker.model.transaction.Transaction) {
        currencyAverage = transaction.currency.toDouble()
        fromAmountTotal = transaction.fromAmount.toDouble()
        fromUnit = transaction.fromUnit
        systemDate = Date(System.currentTimeMillis())
        toAmountTotal = transaction.toAmount.toDouble()
        toUnit = transaction.toUnit
    }

    fun toHomeStockListItem(currencies: Map<String, Double>): HomeStockListItem {
        return HomeStockListItem(
            toUnit,
            String.format("%.2f", toAmountTotal),
            String.format("%.2f", fromAmountTotal),
            fromUnit,
            String.format("%.2f", currencyAverage),
            calculatePNL(currencies)
        )
    }

    private fun calculatePNL(currencies: Map<String, Double>): Double {
        val cur = currencies[fromUnit]!! / currencies[toUnit]!!
        return ((cur - currencyAverage) / currencyAverage) * 100
    }
}