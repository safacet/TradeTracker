package com.safacet.tradetracker.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safacet.tradetracker.R
import com.safacet.tradetracker.databinding.FragmentHomeBinding
import com.safacet.tradetracker.view.adapter.CustomBindingAdapter
import com.safacet.tradetracker.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}