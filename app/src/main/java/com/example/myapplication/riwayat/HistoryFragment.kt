package com.example.myapplication.riwayat

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.FragmentRiwayatBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private lateinit var tableAdapter: TableHistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel
    private val binding get() = _binding!!

    private val fetchHandler = Handler()
    private val fetchRunnable = object : Runnable {
        override fun run() {
            historyViewModel.loadTableData()
            fetchHandler.postDelayed(this, 10000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        val view = binding.root

        tableAdapter = TableHistoryAdapter(emptyList())
        binding.rvHistory.layoutManager = GridLayoutManager(requireContext(), 2)


        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.getTableData().observe(viewLifecycleOwner) { tableList ->
            tableAdapter.updateTableList(tableList)
            binding.rvHistory.adapter = tableAdapter
        }

        historyViewModel.loadTableData()
        fetchHandler.postDelayed(fetchRunnable, 20000)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding
        _binding = null
    }

}