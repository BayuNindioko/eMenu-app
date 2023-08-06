package com.example.myapplication.riwayat


import HistoryAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.Items
import com.example.myapplication.data.OrderResponse
import com.example.myapplication.databinding.ActivityRiwayatByTableBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatByTable : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatByTableBinding
    private lateinit var riwayatAdapter: HistoryAdapter
    private lateinit var itemsList: List<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatByTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.riwayat)
        }

        riwayatAdapter = HistoryAdapter(emptyList()){ order ->

        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = riwayatAdapter

        loadRiwayat()

    }

    private fun loadRiwayat() {
        val number = intent.getStringExtra("NUMBER_KEY")

        val apiService = ApiConfig().getApiService()
        number?.let {
            apiService.getHistoryByTable(it).enqueue(object : Callback<List<OrderResponse>> {
                override fun onResponse(
                    call: Call<List<OrderResponse>>,
                    response: Response<List<OrderResponse>>
                ) {
                    if (response.isSuccessful) {

                        val orderList = response.body()
                        orderList?.let { orders ->
                            val allItemsList = orders.flatMap { order -> order.order_items }

                            allItemsList.forEach { item ->
                                Log.d("FotoDebug", "Item ID: ${item.id}, Foto: ${item.item.foto}")
                            }
                            if (allItemsList.isNotEmpty()) {

                                val riwayatAdapter = HistoryAdapter(allItemsList) { order ->

                                }
                                binding.recyclerView.adapter = riwayatAdapter
                            } else {
                                Toast.makeText(this@RiwayatByTable, "No data available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@RiwayatByTable, "Failed to get data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                    Toast.makeText(this@RiwayatByTable, "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}