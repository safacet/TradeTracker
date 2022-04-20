package com.safacet.tradetracker.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safacet.tradetracker.model.stock.Stock

class SharedViewModel: ViewModel() {
    val stocks = MutableLiveData<List<Stock>>(emptyList())
}