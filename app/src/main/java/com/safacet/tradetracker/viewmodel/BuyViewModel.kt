package com.safacet.tradetracker.viewmodel

import android.app.DatePickerDialog
import android.view.View
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.stock.Stock
import com.safacet.tradetracker.model.transaction.Transaction
import com.safacet.tradetracker.utils.SUPPORTED_SYMBOLS
import com.safacet.tradetracker.utils.dateFormat
import java.lang.NumberFormatException
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
    val toastMessage = MutableLiveData<Int>(null)
    val buyBtnClickable = MutableLiveData(true)
    val autoCompleteArray = MutableLiveData(arrayOf("---"))

    private var timestampDate: Date
    private var stock: Stock

    init {
        date.value = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Calendar.getInstance().time)
        timestampDate = Date(System.currentTimeMillis())
        stock = Stock(currentUser!!.email.toString())

        val autoCompleteList= mutableListOf<String>()
        SUPPORTED_SYMBOLS.map {
            autoCompleteList.add(it.key)
        }
        autoCompleteArray.value = autoCompleteList.toTypedArray()
    }

    fun afterTextChanged() {
        if(isFromChecked.value!!) {
            if(currency.value.isNullOrEmpty() || fromAmount.value.isNullOrEmpty()) {
                toAmount.value = ""
                return
            }
            val cur = currency.value!!.toDoubleOrNull()
            val from = fromAmount.value!!.toDoubleOrNull()
            if (cur == null || from == null || cur == 0.0) {
                toAmount.value = ""
                return
            }
            toAmount.value = DecimalFormat("#.###").format(from / cur)
        } else {
            if(currency.value.isNullOrEmpty() || toAmount.value.isNullOrEmpty()) {
                fromAmount.value = ""
                return
            }
            val cur = currency.value!!.toDoubleOrNull()
            val toVal = toAmount.value!!.toDoubleOrNull()
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
        timestampDate = Date(newDate.timeInMillis)
    }

    fun onBackBtnClicked( view: View ) {
        val navController = view.findNavController()
        navController.popBackStack()
    }

    fun onBuyBtnPressed(v: View) {
        val toSymbol = SUPPORTED_SYMBOLS[toUnit.value]
        val fromSymbol = SUPPORTED_SYMBOLS[fromUnit.value]
        if(toSymbol.isNullOrEmpty() || fromSymbol.isNullOrEmpty()) {
            toUnit.value = ""
            fromUnit.value = ""
            toastMessage.value = R.string.unsupported_symbol
            return
        }

        buyBtnClickable.value = false
        v.alpha = 0.5F
        val userEmail = currentUser!!.email.toString()
        val transaction =
            Transaction("buy", userEmail)

        try {
            toAmount.value?.toDouble()
            fromAmount.value?.toDouble()
            currency.value?.toDouble()
            if(!commissionFee.value.isNullOrEmpty()) {
                commissionFee.value?.toDouble()
            }
        } catch (e: NumberFormatException) {
            toastMessage.value = R.string.not_valid_number
            return
        }

        transaction.toAmount = if(toAmount.value.isNullOrEmpty()) "" else toAmount.value!!
        transaction.fromAmount = if(fromAmount.value.isNullOrEmpty()) "" else fromAmount.value!!
        transaction.currency = if(currency.value.isNullOrEmpty()) "" else currency.value!!
        transaction.commissionFee =if(commissionFee.value.isNullOrEmpty()) "" else commissionFee.value!!
        transaction.fromUnit = fromSymbol
        transaction.toUnit = toSymbol
        transaction.tranDate = timestampDate
        transaction.profit = if(commissionFee.value.isNullOrEmpty()) 0.0 else -commissionFee.value!!.toDouble()

        val db = Firebase.firestore

        db.collection("Transaction").document().set(transaction).addOnSuccessListener {
            db.collection("Stock")
                .whereEqualTo("userEmail", userEmail)
                .whereEqualTo("fromUnit", transaction.fromUnit)
                .whereEqualTo("toUnit", transaction.toUnit).get().addOnSuccessListener { documents ->
                    if(documents.isEmpty) {
                        stock.onFirstBuy(transaction)
                        db.collection("Stock").document().set(stock).addOnSuccessListener {
                            toastMessage.value = R.string.successful_transaction
                            onBackBtnClicked(v)
                        }.addOnFailureListener {
                            toastMessage.value = R.string.db_error
                            buyBtnClickable.value = true
                            v.alpha = 1F
                        }
                    } else {
                        val document = documents.first()
                        val documentName = document.id
                        val data = calculateUpdatedStock(document, transaction)
                        db.collection("Stock").document(documentName)
                            .set(data, SetOptions.merge()).addOnSuccessListener {
                                toastMessage.value = R.string.successful_transaction
                                onBackBtnClicked(v)
                            }.addOnFailureListener{
                                toastMessage.value = R.string.db_error
                                buyBtnClickable.value = true
                                v.alpha = 1F
                            }
                    }
                }
        }.addOnFailureListener {
            toastMessage.value = R.string.db_error
            buyBtnClickable.value = true
            v.alpha = 1F
        }
    }

    private fun calculateUpdatedStock(document: QueryDocumentSnapshot, transaction: Transaction): HashMap<String, Any> {
        stock = document.toObject(Stock::class.java)
        val fromAmountTotal = stock.fromAmountTotal + transaction.fromAmount.toDouble()
        val toAmountTotal = stock.toAmountTotal + transaction.toAmount.toDouble()
        val currencyAverage = fromAmountTotal / toAmountTotal
        stock.systemDate = Date(System.currentTimeMillis())

        return hashMapOf(
            "currencyAverage" to currencyAverage,
            "fromAmountTotal" to fromAmountTotal,
            "toAmountTotal" to toAmountTotal,
            "systemDate" to stock.systemDate
        )
    }
}