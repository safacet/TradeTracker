package com.safacet.tradetracker.viewmodel

import androidx.annotation.LayoutRes
import com.safacet.tradetracker.R
import com.safacet.tradetracker.utils.HOME_HISTORY_VIEW_TYPE
import com.safacet.tradetracker.utils.HOME_STOCK_VIEW_TYPE

interface ItemViewModel {
    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = 0
}

class HomeStockListItem(
    val toName: String,
    val toQuantity: String,
    val fromAmount: String,
    val fromName: String,
    val currency: String
): ItemViewModel {
    override val layoutId: Int = R.layout.recycler_view_stock_item
    override val viewType: Int = HOME_STOCK_VIEW_TYPE
}

class HomeHistoryListItem(
    val toName: String,
    val toAmount: String,
    val currency: String,
    val date: String,
    val isBuy: Boolean
):ItemViewModel {
    override val layoutId = R.layout.recycler_view_history_item
    override val viewType = HOME_HISTORY_VIEW_TYPE
}