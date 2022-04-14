package com.safacet.tradetracker.model.transaction

import com.google.firebase.firestore.PropertyName
import com.safacet.tradetracker.utils.dateFormat
import com.safacet.tradetracker.viewmodel.HomeHistoryListItem
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class Transaction(
    @PropertyName("transactionType") val transactionType: String = "",
    @PropertyName("userEmail") val userEmail: String = "",
    @PropertyName("systemDate") val systemDate: Date = Date(0)
) {
    @PropertyName("commissionFee") var commissionFee: String = ""
    @PropertyName("currency") var currency: String = ""
    @PropertyName("fromAmount") var fromAmount: String = ""
    @PropertyName("fromUnit") var fromUnit: String = ""
    @PropertyName("profit") var profit: Double = 0.0
    @PropertyName("toAmount") var toAmount: String = ""
    @PropertyName("toUnit") var toUnit: String = ""
    @PropertyName("tranDate") var tranDate= Date(0)

    fun toHomeHistoryListItem(): HomeHistoryListItem {
        val date = SimpleDateFormat(dateFormat, Locale.getDefault()).format(tranDate)
        val isBuy = transactionType == "buy"
        return HomeHistoryListItem(toUnit, toAmount, currency, date, isBuy)
    }
}