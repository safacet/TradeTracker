package com.safacet.tradetracker.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.safacet.tradetracker.R
import com.safacet.tradetracker.model.transaction.Transaction
import com.safacet.tradetracker.viewmodel.ItemViewModel
import java.util.function.Consumer

class CustomBindingAdapter {

    companion object {
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
                    isFromChecked.value = (view.id == R.id.etFromAmount) || (view.id == R.id.etSellFromAmount)
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

        @BindingAdapter("app:itemViewModel")
        @JvmStatic fun bindItemViewModels(recyclerView: RecyclerView, itemViewModels: List<ItemViewModel>) {
            val adapter = getOrCreateAdapter(recyclerView)
            adapter.updateItems(itemViewModels)
        }

        private fun getOrCreateAdapter(recyclerView: RecyclerView): BindableRecyclerViewAdapter {
            return if(recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter)
                recyclerView.adapter as BindableRecyclerViewAdapter
            else {
                val bindableRecyclerAdapter = BindableRecyclerViewAdapter()
                recyclerView.adapter = bindableRecyclerAdapter
                bindableRecyclerAdapter
            }
        }

        @BindingAdapter("app:isBuy")
        @JvmStatic fun setImageAsset(imageView: ImageView, isBuy: Boolean) {
            if (isBuy)
                imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_arrow_downward))
            else
                imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_arrow_upward))
        }

        @BindingAdapter("app:selectedTab")
        @JvmStatic fun onTabChange(tl: TabLayout, loadData: MutableLiveData<Int>) {
            tl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    loadData.value = tab?.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    return
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    return
                }

            })
        }

        @BindingAdapter("app:sellPlaceHolder")
        @JvmStatic fun setPlaceholder(v: View, placeHolder: String) {
            if (v !is EditText) {
                (v as TextView).text = v.context.getString(R.string.sell_from_to, placeHolder)
            } else {
                v.hint = v.context.getString(R.string.sell_from_to, placeHolder)
            }
        }

        @BindingAdapter("app:spinnerAdapter")
        @JvmStatic fun spinnerAdapter(spinner: Spinner, itemArray: Array<String>) {
            val ad = ArrayAdapter(spinner.context, R.layout.spinner_item, itemArray)
            ad.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = ad
        }

        @BindingAdapter("spinnerListener")
        @JvmStatic fun spinnerListener(spinner: Spinner, selectedItem: MutableLiveData<Int>) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItem.value = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    return
                }

            }
        }
    }
}