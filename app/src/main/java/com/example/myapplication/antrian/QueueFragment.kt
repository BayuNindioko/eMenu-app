package com.example.myapplication.antrian

import PesananAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.TableResponse
import com.example.myapplication.databinding.FragmentAntrianBinding
import com.example.myapplication.pesanan.PesananActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueueFragment : Fragment() {
    private var _binding: FragmentAntrianBinding? = null
    private lateinit var tableAdapter: TableAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAntrianBinding.inflate(inflater, container, false)
        val view = binding.root



        tableAdapter = TableAdapter(emptyList())
        binding.rvTable.layoutManager = LinearLayoutManager(requireContext())


        binding.historyBut.setOnClickListener {
            val intent = Intent(requireContext(), PesananActivity::class.java)
            startActivity(intent)
        }


        val apiService = ApiConfig().getApiService()

        apiService.getTable().enqueue(object : Callback<TableResponse> {
            override fun onResponse(call: Call<TableResponse>, response: Response<TableResponse>) {
                if (response.isSuccessful) {
                    val tableList = response.body()?.tableResponse
                    tableList?.let {
                        tableAdapter = TableAdapter(it)
                        binding.rvTable.adapter = tableAdapter
                    }
                } else {
                    Log.d("aldo", "${response.code()}")
                    Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            }



            override fun onFailure(call: Call<TableResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to makan", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}