package com.safacet.tradetracker.model.stock

import com.google.firebase.firestore.PropertyName
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
        return HomeStockListItem(toUnit, toAmountTotal.toString(), fromAmountTotal.toString(), fromUnit, currencyAverage.toString())
    }
}