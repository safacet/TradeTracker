package com.safacet.tradetracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.safacet.tradetracker.R
import com.safacet.tradetracker.databinding.FragmentHomeBinding
import com.safacet.tradetracker.model.SharedViewModel
import com.safacet.tradetracker.utils.STOCK_TAB_POSITION
import com.safacet.tradetracker.view.adapter.CustomBindingAdapter
import com.safacet.tradetracker.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val model: SharedViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewModel.toastMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                showToastMessage(it)
            }
        }

        homeViewModel.loadRecyclerViewData.observe(viewLifecycleOwner) {
            if (it != null) {
                loadData(it)
            }
        }

        homeViewModel.usersStocks.observe(viewLifecycleOwner) {
            if(it != null) {
                model.stocks.value = it
            }
        }
        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadRecyclerViewData.value?.let { loadData(it) }
    }

    private fun loadData(id: Int) {
        if (id == STOCK_TAB_POSITION)
            homeViewModel.loadStockData()
        else
            homeViewModel.loadHistoryData()
    }

    private fun showToastMessage(id: Int) {
        Toast.makeText(activity, resources.getString(id), Toast.LENGTH_LONG).show()
    }

}
