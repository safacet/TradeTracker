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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.stock.Stock
import com.safacet.tradetracker.model.transaction.Transaction
import com.safacet.tradetracker.utils.STOCK_TAB_POSITION
import com.safacet.tradetracker.utils.getCurrency
import com.safacet.tradetracker.view.ui.HomeFragmentDirections
import org.json.JSONObject

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
    var currentCurrencies = MutableLiveData(mapOf<String, Double>())
    val totalValue = MutableLiveData("---")
    val isTotalValProfit = MutableLiveData(true)
    val potentialPNL = MutableLiveData("---")
    val isPotentialPNLProfit = MutableLiveData(true)
    val realPNL = MutableLiveData("---")
    val isRealPNLProfil = MutableLiveData(true)

    private val _data = MutableLiveData<List<ItemViewModel>>(emptyList())

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _data.value = emptyList()
        isLoading.value = true
        val db = Firebase.firestore

        db.collection("rates")
            .document("latest").get().addOnSuccessListener { document ->
                if (document.exists()) {
                    currentCurrencies.value = document.data as Map<String, Double>
                }
            }
    }

    fun loadStockData() {
        if (currentCurrencies.value.isNullOrEmpty()) {
            return
        }
        val db = Firebase.firestore
        val userEmail = Firebase.auth.currentUser?.email.toString()
        db.collection("Stock")
            .whereEqualTo("userEmail", userEmail)
            .orderBy("systemDate", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    isLoading.value = false
                    toastMessage.value = R.string.no_data_on_listing
                    return@addOnSuccessListener
                } else {
                    val stockList: MutableList<ItemViewModel> = mutableListOf()
                    usersStocks.value = mutableListOf()
                    for (document in documents) {
                        val stock: Stock = document.toObject(Stock::class.java)
                        usersStocks.value?.add(stock)
                        stockList.add(stock.toHomeStockListItem(currentCurrencies.value!!))
                        calculateTopBar()

                    }
                    isLoading.value = false
                    _data.value = stockList
                }
            }.addOnFailureListener {
                toastMessage.value = R.string.fetching_error
            }
    }

    private fun calculateTopBar() {
        calculateTotalValue()
        calculatePotentialPNL()
        calculateRealPNL()
    }

    private fun calculateRealPNL() {
        var pNL = 0.0
        val db = Firebase.firestore
        val userEmail = Firebase.auth.currentUser?.email.toString()
        db.collection("Transaction")
            .whereEqualTo("userEmail", userEmail).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = document.data
                    pNL += data["profit"] as Double * getCurrency(
                        currentCurrencies.value!![baseCurrencySelected.value!!]!!,
                        currentCurrencies.value!![(data["fromUnit"])]!!
                    )
                }
                realPNL.value = String.format("%.2f", pNL)
                isRealPNLProfil.value = (pNL > 0)
            }
    }

    private fun calculatePotentialPNL() {
        var pNL = 0.0
        for (stock in usersStocks.value!!) {
            val percent = stock.calculatePNL(currentCurrencies.value!!) / 100
            pNL += stock.fromAmountTotal * getCurrency(
                currentCurrencies.value!![baseCurrencySelected.value!!]!!,
                currentCurrencies.value!![stock.fromUnit]!!
            ) * percent
        }
        potentialPNL.value = String.format("%.2f", pNL)
        isPotentialPNLProfit.value = (pNL > 0)
    }

    private fun calculateTotalValue() {
        var totalVal = 0.0
        for (stock in usersStocks.value!!) {
            totalVal += stock.toAmountTotal * getCurrency(
                currentCurrencies.value!![baseCurrencySelected.value!!]!!,
                currentCurrencies.value!![stock.toUnit]!!
            )
        }
        totalValue.value = String.format("%.2f", totalVal)
        isTotalValProfit.value = (totalVal > 0)
    }


    fun loadHistoryData() {
        _data.value = emptyList()
        isLoading.value = true
        val db = Firebase.firestore
        val userEmail = Firebase.auth.currentUser?.email.toString()

        db.collection("Transaction")
            .whereEqualTo("userEmail", userEmail)
            .orderBy("tranDate", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    isLoading.value = false
                    toastMessage.value = R.string.no_data_on_listing
                    return@addOnSuccessListener
                } else {
                    val historyList: MutableList<ItemViewModel> = mutableListOf()
                    for (document in documents) {
                        val transaction: Transaction = document.toObject(Transaction::class.java)
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

        if(!currentCurrencies.value.isNullOrEmpty())
            calculateTopBar()
    }
}