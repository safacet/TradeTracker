package com.safacet.tradetracker.view.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.safacet.tradetracker.R
import com.safacet.tradetracker.databinding.BuyFragmentBinding
import com.safacet.tradetracker.viewmodel.BuyViewModel

class BuyFragment : Fragment() {

    private lateinit var viewModel: BuyViewModel
    private var _binding: BuyFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[BuyViewModel::class.java]
        _binding = BuyFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.toastMessage.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                showToastMessage(it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToastMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }
}