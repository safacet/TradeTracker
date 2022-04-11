package com.safacet.tradetracker.model.transaction

import java.sql.Timestamp
import java.util.*

data class Transaction(
    val transactionType: String,
    val userEmail: String,
    val systemDate: Timestamp
) {
    var commissionFee: String = ""
    var currency: String = ""
    var fromAmount: String = ""
    var fromUnit: String = ""
    var profit: Double = 0.0
    var toAmount: String = ""
    var toUnit: String = ""
    var tranDate= Date(0)
}