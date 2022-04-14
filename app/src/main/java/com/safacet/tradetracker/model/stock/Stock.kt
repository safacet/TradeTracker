package com.safacet.tradetracker.model.stock

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.Transaction
import com.safacet.tradetracker.viewmodel.HomeStockListItem
import java.sql.Timestamp
import java.util.*

data class Stock (
    @PropertyName("currencyAverage") var currencyAverage: Double = 0.0,
    @PropertyName("fromAmountTotal") var fromAmountTotal: Double = 0.0,
    @PropertyName("fromUnit") var fromUnit: String = "",
    @PropertyName("systemDate") var systemDate: Date = Date(0),
    @PropertyName("toAmountTotal") var toAmountTotal: Double = 0.0,
    @PropertyName("toUnit") var toUnit: String = "",
    @PropertyName("userEmail") var userEmail: String = ""
        ) {

    fun toHomeStockListItem(): HomeStockListItem {
        return HomeStockListItem(toUnit, String.format("%.2f", toAmountTotal), String.format("%.2f", fromAmountTotal), fromUnit, String.format("%.2f", currencyAverage))
    }
}