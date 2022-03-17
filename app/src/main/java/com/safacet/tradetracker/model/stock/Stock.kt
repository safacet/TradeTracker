package com.safacet.tradetracker.model.stock

import java.sql.Timestamp

class Stock (
    var currencyAverage: Float,
    var fromAmountTotal: Float,
    var fromUnit: String,
    var systemDate: Timestamp,
    var toAmountTotal: Float,
    var toUnit: String,
    var userEmail: String
        ) {
}