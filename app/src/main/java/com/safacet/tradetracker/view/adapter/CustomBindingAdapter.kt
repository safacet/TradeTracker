package com.safacet.tradetracker.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.MutableLiveData
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.transaction.Transaction
import java.util.function.Consumer

class CustomBindingAdapter {

    companion object {
        @BindingAdapter("android:stillLoading")
        @JvmStatic
        fun showHide(view: View, show: Boolean) {
            if (show) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @BindingAdapter("android:displayText")
        @JvmStatic
        fun displayText(v: TextView, id:Int?) {
            if (id != null) {
                v.text = v.context.getString(id)
            }
        }

        @BindingAdapter("app:onFocus")
        @JvmStatic fun onFocus(view: View, isFromChecked: MutableLiveData<Boolean>) {
            view.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    isFromChecked.value = view.id == R.id.etFromAmount
                }
            }
        }

        @BindingAdapter("android:text")
        @JvmStatic
        fun setText(view: EditText, value: String?) {

            if ((view.text.toString() != value)) {
                view.setText(value)
                view.setSelection(view.text.length)
            }
        }

        @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
        @JvmStatic fun getText(view: EditText): String {
            return view.text.toString()
        }

        @BindingAdapter("app:dateClick")
        @JvmStatic fun onDateClicked(v: EditText, callback: (EditText) -> Any?) {
            callback(v)
        }
    }
}