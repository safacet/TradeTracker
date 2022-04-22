package com.safacet.tradetracker.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.core.os.HandlerCompat.postDelayed
import androidx.databinding.BaseObservable
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.stock.Stock
import com.safacet.tradetracker.model.transaction.Transaction
import com.safacet.tradetracker.utils.STOCK_TAB_POSITION
import com.safacet.tradetracker.view.ui.HomeFragmentDirections

class HomeViewModel : ViewModel() {

    var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var baseCurrencySelected = MutableLiveData<String>().apply { value = "TRY" }
    var baseCurrencyTop = MutableLiveData<String>().apply { value = "EUR" }
    var baseCurrencyBottom = MutableLiveData<String>().apply { value = "USD" }
    val data: LiveData<List<ItemViewModel>>
    get() = _data
    val toastMessage = MutableLiveData<Int>()
    val loadRecyclerViewData = MutableLiveData<Int>().apply { value = STOCK_TAB_POSITION }
    val usersStocks = MutableLiveData<MutableList<Stock>>(mutableListOf())
    val overScrolled = MutableLiveData(false)

    private val _data = MutableLiveData<List<ItemViewModel>>(emptyList())

    init {
        loadStockData()
    }

    fun loadStockData() {
        _data.value = emptyList()
        isLoading.value = true
        val db = Firebase.firestore
        val userEmail = Firebase.auth.currentUser?.email.toString()

        db.collection("Stock")
            .whereEqualTo("userEmail", userEmail)
            .orderBy("systemDate", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
                if(documents.isEmpty) {
                    isLoading.value = false
                    toastMessage.value = R.string.no_data_on_listing
                    return@addOnSuccessListener
                } else {
                    val stockList : MutableList<ItemViewModel> = mutableListOf()
                    usersStocks.value = mutableListOf()
                    for (document in documents) {
                        val stock : Stock = document.toObject(Stock::class.java)
                        usersStocks.value?.add(stock)
                        stockList.add(stock.toHomeStockListItem())
                    }
                    isLoading.value = false
                    _data.value = stockList
                }
            }.addOnFailureListener {
                toastMessage.value = R.string.fetching_error
            }
    }

    fun loadHistoryData() {
        _data.value = emptyList()
        isLoading.value = true
        val db = Firebase.firestore
        val userEmail = Firebase.auth.currentUser?.email.toString()

        db.collection("Transaction")
            .whereEqualTo("userEmail", userEmail)
            .orderBy("tranDate", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
                if(documents.isEmpty) {
                    isLoading.value = false
                    toastMessage.value = R.string.no_data_on_listing
                    return@addOnSuccessListener
                } else {
                    val historyList: MutableList<ItemViewModel> = mutableListOf()
                    for (document in documents) {
                        val transaction : Transaction = document.toObject(Transaction::class.java)
                        historyList.add(transaction.toHomeHistoryListItem())
                    }
                    isLoading.value = false
                    _data.value = historyList
                }
            }.addOnFailureListener {
                toastMessage.value = R.string.fetching_error
            }
    }


    fun onPlusClicked(view: View) {
        val action = HomeFragmentDirections
            .actionNavigationHomeToBuyFragment()
        view.findNavController().navigate(action)
    }

    fun onMinusClicked(view: View) {
        val action = HomeFragmentDirections
            .actionNavigationHomeToSellFragment()
        view.findNavController().navigate(action)
    }

    fun onBaseCurrencyClicked(baseCurrency: MutableLiveData<String>) {
        val mid = baseCurrency.value
        baseCurrency.value = baseCurrencySelected.value
        baseCurrencySelected.value = mid
    }
}