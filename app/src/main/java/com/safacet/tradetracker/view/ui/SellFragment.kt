package com.safacet.tradetracker.view.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.safacet.tradetracker.R
import com.safacet.tradetracker.databinding.SellFragmentBinding
import com.safacet.tradetracker.model.SharedViewModel
import com.safacet.tradetracker.viewmodel.SellViewModel

class SellFragment : Fragment() {

    private lateinit var viewModel: SellViewModel
    private var _binding: SellFragmentBinding? = null
    val binding get() = _binding!!

    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SellViewModel::class.java]
        _binding = SellFragmentBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        model.stocks.observe(viewLifecycleOwner) {
            viewModel.usersStocks.value = it
            viewModel.processStocks()
        }

        viewModel.selectedSpinnerItem.observe(viewLifecycleOwner) {
            if(it != null)
                viewModel.onSpinnerItemSelected(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}