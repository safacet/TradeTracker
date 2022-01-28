package com.safacet.tradetracker.view.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.safacet.tradetracker.databinding.PerformanceFragmentBinding
import com.safacet.tradetracker.viewmodel.PerformanceViewModel

class PerformanceFragment : Fragment() {

    private lateinit var viewModel: PerformanceViewModel
    private var _binding: PerformanceFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[PerformanceViewModel::class.java]

        _binding = PerformanceFragmentBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.tvPerformance
        viewModel.text.observe(viewLifecycleOwner, { data->
            textView.text = data
        })
        return  root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}