package com.example.myapplication.riwayat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.TableResponseItem
import com.example.myapplication.databinding.FragmentRiwayatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private lateinit var tableAdapter: TableHistoryAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        val view = binding.root

        tableAdapter = TableHistoryAdapter(emptyList())
        binding.rvHistory.layoutManager = GridLayoutManager(requireContext(), 2)


        val apiService = ApiConfig().getApiService()

        apiService.getTable().enqueue(object : Callback<List<TableResponseItem>> {
            override fun onResponse(call: Call<List<TableResponseItem>>, response: Response<List<TableResponseItem>>) {
                if (response.isSuccessful) {
                    val tableList = response.body()
                    tableList?.let {
                        tableAdapter = TableHistoryAdapter(tableList)
                        binding.rvHistory.adapter = tableAdapter
                    }
                } else {
                    Log.d("aldo", "${response.code()}")
                    Toast.makeText(requireContext(), "Selamat Datang", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TableResponseItem>>, t: Throwable) {
                Log.e("aldo", "Error: ${t.message}")
                t.printStackTrace()
                Toast.makeText(requireContext(), "Failed to makan", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding
        _binding = null
    }

}