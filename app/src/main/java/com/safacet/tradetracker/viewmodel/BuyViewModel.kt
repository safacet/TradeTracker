package com.safacet.tradetracker.viewmodel

import android.app.DatePickerDialog
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.transaction.Transaction
import com.safacet.tradetracker.view.adapter.CustomBindingAdapter
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class BuyViewModel : ViewModel() {
    private val currentUser = Firebase.auth.currentUser
    val commissionFee = MutableLiveData<String?>(null)
    val currency = MutableLiveData<String?>(null)
    val fromAmount = MutableLiveData<String?>(null)
    val toAmount = MutableLiveData<String?>(null)
    val fromUnit = MutableLiveData<String?>(null)
    val toUnit = MutableLiveData<String?>(null)
    val date = MutableLiveData<String?>(null)
    val isFromChecked = MutableLiveData(true)
    val toastMessage = MutableLiveData<String>(null)

    init {
        date.value = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Calendar.getInstance().time)
    }

    companion object {
        const val dateFormat = "dd MMM yyyy"
    }

    fun afterTextChanged() {
        if(isFromChecked.value!!) {
            if(currency.value.isNullOrEmpty() || fromAmount.value.isNullOrEmpty()) {
                toAmount.value = ""
                return
            }
            val cur = currency.value!!.toFloatOrNull()
            val from = fromAmount.value!!.toFloatOrNull()
            if (cur == null || from == null || cur == 0f) {
                toAmount.value = ""
                return
            }
            toAmount.value = DecimalFormat("#.###").format(from / cur)
        } else {
            if(currency.value.isNullOrEmpty() || toAmount.value.isNullOrEmpty()) {
                fromAmount.value = ""
                return
            }
            val cur = currency.value!!.toFloatOrNull()
            val toVal = toAmount.value!!.toFloatOrNull()
            if (cur == null || toVal == null) {
                fromAmount.value = ""
                return
            }
            fromAmount.value = DecimalFormat("#.###").format(toVal * cur)
        }
    }

    fun onDateClick(v: View) {
        val dialog = DatePickerDialog(v.context)
            dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                onDatePicked(year, month, dayOfMonth)
            }
        dialog.show()
    }

    private fun onDatePicked(year: Int, month: Int, day: Int) {
        val newDate = Calendar.getInstance()
        newDate.set(year, month, day)
        date.value = SimpleDateFormat(dateFormat, Locale.getDefault()).format(newDate.time)
    }
}