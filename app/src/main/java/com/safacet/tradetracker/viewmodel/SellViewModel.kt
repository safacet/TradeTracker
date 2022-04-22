package com.safacet.tradetracker.viewmodel

import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.stock.Stock
import com.safacet.tradetracker.model.transaction.Transaction
import com.safacet.tradetracker.utils.InputTypes
import com.safacet.tradetracker.utils.dateFormat
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.meta.When
import kotlin.collections.HashMap

class SellViewModel : ViewModel() {
    private val currentUser = Firebase.auth.currentUser
    val isFromChecked = MutableLiveData(false)
    val fromAmount = MutableLiveData<String>(null)
    val toAmount = MutableLiveData<String>(null)
    val currency = MutableLiveData<String>(null)
    val commission = MutableLiveData<String>(null)

    val spinnerArray = MutableLiveData(arrayOf("----------------"))
    val selectedSpinnerItem = MutableLiveData(0)
    val from = MutableLiveData("From")
    val to = MutableLiveData("To")
    val date = MutableLiveData<String?>(null)
    val usersStocks = MutableLiveData<List<Stock>>(emptyList())
    val sellBtnClickable = MutableLiveData(true)

    var ownedAmountOfTo: Double = 0.0
    var boughtCurrency: Double = 0.0

    val toastMessage = MutableLiveData(0)

    private var timestampDate: Date
    private var stock: Stock

    init {
        date.value = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Calendar.getInstance().time)
        timestampDate = Date(System.currentTimeMillis())

        stock = Stock(currentUser!!.email.toString())
    }

    fun processStocks() {
        if(!usersStocks.value.isNullOrEmpty()){
            ownedAmountOfTo = usersStocks.value?.get(0)?.toAmountTotal ?: 0.0
            boughtCurrency = usersStocks.value?.get(0)?.currencyAverage ?: 0.0

            val stocks = usersStocks.value ?: emptyList()
            val spinnerList = mutableListOf<String>()
            for (stock in stocks) {
                val string = " ${stock.toUnit}/${stock.fromUnit} "
                spinnerList.add(string)
            }
            spinnerArray.value = spinnerList.toTypedArray()
        } else {
            sellBtnClickable.value = false
            //TODO: Toast Message that indicates there is nothing to sell
        }

    }

    fun onSpinnerItemSelected(position: Int) {
        if(usersStocks.value.isNullOrEmpty())
            return
        isFromChecked.value = false
        val selectedStock = usersStocks.value!![position]
        from.value = selectedStock.fromUnit
        to.value = selectedStock.toUnit
        toAmount.value = selectedStock.toAmountTotal.toString()

        ownedAmountOfTo = selectedStock.toAmountTotal
        boughtCurrency = selectedStock.currencyAverage
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

    fun onSellBtnClicked(v: View) {
        sellBtnClickable.value = false
        v.alpha = 0.5F

        val userEmail = currentUser!!.email.toString()
        val transaction = Transaction("sell", userEmail)


        when(verifyInputs()) {
            InputTypes.OVER_TO_AMOUNT_ERROR -> {
                toastMessage.value = R.string.over_to_amount
            }
            InputTypes.NUMBER_FORMAT_ERROR -> {
                toastMessage.value = R.string.number_format
            }
            InputTypes.BLANKED_FIELD -> {
                toastMessage.value = R.string.blank_field_sell
            }

            InputTypes.INPUT_CORRECT -> {
                saveTransaction(v, transaction)
            }
        }
        sellBtnClickable.value = true
        v.alpha = 1F
    }

    private fun saveTransaction(v: View, transaction: Transaction) {
        val commissionNumber = if(commission.value.isNullOrEmpty()) 0.0 else commission.value!!.toDouble()
        transaction.toAmount = if(toAmount.value.isNullOrEmpty()) "" else toAmount.value!!
        transaction.fromAmount = if(fromAmount.value.isNullOrEmpty()) "" else fromAmount.value!!
        transaction.currency = if(currency.value.isNullOrEmpty()) "" else currency.value!!
        transaction.commissionFee =if(commission.value.isNullOrEmpty()) "" else commission.value!!
        transaction.toUnit = to.value.toString()
        transaction.fromUnit = from.value.toString()
        transaction.tranDate = timestampDate
        transaction.profit =
            ((currency.value!!.toDouble() - boughtCurrency) * toAmount.value!!.toDouble()) - commissionNumber

        val db = Firebase.firestore
        db.collection("Transaction").document().set(transaction).addOnSuccessListener {
            db.collection("Stock")
                .whereEqualTo("userEmail", transaction.userEmail)
                .whereEqualTo("fromUnit", transaction.fromUnit)
                .whereEqualTo("toUnit", transaction.toUnit).get().addOnSuccessListener { documents ->
                    if(!documents.isEmpty) {
                        val document = documents.first()
                        val documentName = document.id
                        val data = calculateUpdatedStock(document, transaction)
                        if (stock.toAmountTotal != 0.0) {
                            db.collection("Stock").document(documentName)
                                .set(data, SetOptions.merge()).addOnSuccessListener {
                                    toastMessage.value =R.string.successful_transaction
                                    onBackBtnClicked(v)
                                }.addOnFailureListener{
                                    toastMessage.value = R.string.db_error
                                }
                        } else {
                            db.collection("Stock").document(documentName)
                                .delete().addOnSuccessListener {
                                    toastMessage.value =R.string.successful_transaction
                                    onBackBtnClicked(v)
                                }.addOnFailureListener {
                                    toastMessage.value = R.string.db_error
                                }
                        }

                    }
                }
        }.addOnFailureListener {
            toastMessage.value = R.string.db_error
        }
    }


    private fun calculateUpdatedStock(document: QueryDocumentSnapshot, transaction: Transaction): HashMap<String, Any> {
        stock = document.toObject(Stock::class.java)
        stock.toAmountTotal = stock.toAmountTotal - transaction.toAmount.toDouble()
        stock.fromAmountTotal = stock.toAmountTotal * stock.currencyAverage
        stock.systemDate = Date(System.currentTimeMillis())

        return hashMapOf(
            "fromAmountTotal" to stock.fromAmountTotal,
            "toAmountTotal" to stock.toAmountTotal,
            "systemDate" to stock.systemDate
        )
    }

    private fun verifyInputs(): InputTypes {
        //Check for necessary fields filled
        if (
            (toAmount.value.isNullOrEmpty() &&
            fromAmount.value.isNullOrEmpty()) ||
            currency.value.isNullOrEmpty()
        ) return InputTypes.BLANKED_FIELD

        //Check for inputs are correct double numbers
        try {
            toAmount.value!!.toDouble()
            fromAmount.value!!.toDouble()
            currency.value!!.toDouble()
            if(!commission.value.isNullOrEmpty()) {
                commission.value!!.toDouble()
            }
        } catch (e: NumberFormatException) {
            return InputTypes.NUMBER_FORMAT_ERROR
        }

        //Check for user trying to sell more than they have
        if(toAmount.value?.toDouble()!! > ownedAmountOfTo) {
            return InputTypes.OVER_TO_AMOUNT_ERROR
        }
        return InputTypes.INPUT_CORRECT
    }
}