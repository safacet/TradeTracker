package com.safacet.tradetracker.viewmodel

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.core.os.HandlerCompat.postDelayed
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.model.stock.Stock
import com.safacet.tradetracker.view.ui.HomeFragmentDirections

class HomeViewModel : ViewModel() {

    var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var baseCurrencySelected = MutableLiveData<String>().apply { value = "TRY" }
    var baseCurrencyTop = MutableLiveData<String>().apply { value = "EUR" }
    var baseCurrencyBottom = MutableLiveData<String>().apply { value = "USD" }
    val data: LiveData<List<ItemViewModel>>
    get() = _data

    private val _data = MutableLiveData<List<ItemViewModel>>(emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        val db = Firebase.firestore
        val userEmail = Firebase.auth.currentUser?.email.toString()

        db.collection("Stock")
            .whereEqualTo("userEmail", userEmail).get().addOnSuccessListener { documents ->
                if(documents.isEmpty) {
                    return@addOnSuccessListener
                } else {
                    val stockList : MutableList<ItemViewModel> = mutableListOf()
                    for (document in documents) {
                        val stock : Stock = document.toObject(Stock::class.java)
                        stockList.add(stock.toHomeStockListItem())
                    }
                    _data.value = stockList
                }
            }
    }


    fun onPlusClicked(view: View) {
        val action = HomeFragmentDirections
            .actionNavigationHomeToBuyFragment()
        view.findNavController().navigate(action)
    }

    fun onBaseCurrencyClicked(baseCurrency: MutableLiveData<String>) {
        val mid = baseCurrency.value
        baseCurrency.value = baseCurrencySelected.value
        baseCurrencySelected.value = mid
    }
}