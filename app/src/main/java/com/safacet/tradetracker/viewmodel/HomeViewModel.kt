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
import com.safacet.tradetracker.view.ui.HomeFragmentDirections

class HomeViewModel : ViewModel() {

    var isLoading = MutableLiveData<Boolean>().apply { value = false }
    var baseCurrencySelected = MutableLiveData<String>().apply { value = "TRY" }
    var baseCurrencyTop = MutableLiveData<String>().apply { value = "EUR" }
    var baseCurrencyBottom = MutableLiveData<String>().apply { value = "USD" }


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