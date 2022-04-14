package com.safacet.tradetracker.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
    }
}