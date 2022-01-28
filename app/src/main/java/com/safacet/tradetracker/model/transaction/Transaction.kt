package com.safacet.tradetracker.model.transaction

import java.sql.Timestamp
import java.util.*

data class Transaction(
    val transactionType: String,
    val userEmail: String,
    val systemDate: Timestamp
) {
    var commissionFee: Float? = null
    var currency: Float = 0f
    var fromAmount: Float = 0f
    var fromUnit: String? = null
    var profit: Float? = null
    var toAmount: Float = 0f
    var toUnit: String? = null

}