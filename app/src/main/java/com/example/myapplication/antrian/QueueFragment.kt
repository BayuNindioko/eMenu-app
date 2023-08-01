package com.example.myapplication.antrian



import android.os.Bundle
import android.os.Handler

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.myapplication.databinding.FragmentAntrianBinding


class QueueFragment : Fragment() {
    private var _binding: FragmentAntrianBinding? = null
    private lateinit var tableAdapter: TableAdapter
    private lateinit var queueViewModel: QueueViewModel
    private val binding get() = _binding!!

    private val fetchHandler = Handler()
    private val fetchRunnable = object : Runnable {
        override fun run() {
            queueViewModel.fetchTableData()
            fetchHandler.postDelayed(this, 20000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrianBinding.inflate(inflater, container, false)
        val view = binding.root

        tableAdapter = TableAdapter(emptyList())
        binding.rvTable.layoutManager = GridLayoutManager(requireContext(), 2)

        queueViewModel = ViewModelProvider(this).get(QueueViewModel::class.java)
        queueViewModel.getTableData().observe(viewLifecycleOwner) { tableList ->
            tableAdapter = TableAdapter(tableList)
            binding.rvTable.adapter = tableAdapter
        }
        queueViewModel.fetchTableData()
        fetchHandler.postDelayed(fetchRunnable, 20000)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
