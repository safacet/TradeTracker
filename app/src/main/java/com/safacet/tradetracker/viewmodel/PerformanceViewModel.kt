package com.safacet.tradetracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PerformanceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Performance fragment"
    }

    val text: LiveData<String> = _text
}