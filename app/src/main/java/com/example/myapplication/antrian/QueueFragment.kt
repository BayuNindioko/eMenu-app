package com.example.myapplication.antrian

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAntrianBinding
import com.example.myapplication.pesanan.PesananActivity

class QueueFragment : Fragment() {
    private var _binding: FragmentAntrianBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAntrianBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.queueBut.setOnClickListener {
            val intent = Intent(requireContext(), PesananActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding
        _binding = null
    }
}