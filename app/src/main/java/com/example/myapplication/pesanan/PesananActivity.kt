package com.example.myapplication.pesanan

import PesananAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.OrderResponse
import com.example.myapplication.data.UpdateResponse
import com.example.myapplication.databinding.ActivityPesananBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding
    private lateinit var pesananAdapter: PesananAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.Pesanan)
        }
        val number = intent.getStringExtra("NUMBER_KEY")
        binding.numberCircle.text = "Meja No : $number"

        pesananAdapter = PesananAdapter(emptyList()) { order ->
            showBottomSheetDialog(order)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = pesananAdapter


        loadPesananData()

    }

    private fun loadPesananData() {
        val number = intent.getStringExtra("NUMBER_KEY")
        val apiService = ApiConfig().getApiService()
        number?.let {
            apiService.getOrderByTable(it).enqueue(object : Callback<List<OrderResponse>> {
                override fun onResponse(
                    call: Call<List<OrderResponse>>,
                    response: Response<List<OrderResponse>>
                ) {
                    if (response.isSuccessful) {
                        val orderList = response.body()
                        orderList?.let { it ->
                            pesananAdapter = PesananAdapter(it) { order ->
                                showBottomSheetDialog(order)
                            }
                            binding.recyclerView.adapter = pesananAdapter
                        }
                    } else {
                        Toast.makeText(this@PesananActivity, "Failed to get data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                    Toast.makeText(this@PesananActivity, "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showBottomSheetDialog(order: OrderResponse) {

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.note_dialogue, null)
        val btnSelesai = view.findViewById<Button>(R.id.idBtnSelesai)
        val decreaseButton = view.findViewById<ImageView>(R.id.decrease_1)
        val increaseButton = view.findViewById<ImageView>(R.id.increase_1)
        val numberTextView = view.findViewById<TextView>(R.id.integer_number_1)

        val textViewMenuName = view.findViewById<TextView>(R.id.textViewMenuName)
        val textViewMenuQty = view.findViewById<TextView>(R.id.textViewMenuQty)
        val textViewMenuQtyDeliv = view.findViewById<TextView>(R.id.textViewMenuQtyDeliv)
        val catatan = view.findViewById<TextView>(R.id.note_order)
        dialog.setCancelable(true)
        dialog.setContentView(view)

        textViewMenuName.text = order.name
        textViewMenuQty.text = "Jumlah pesanan: ${order.quantityOrder}"
        textViewMenuQtyDeliv.text = "Jumlah pesanan diantar: ${order.quantityDelivered}/${order.quantityOrder}"
        catatan.text = order.notes

        var count = order.quantityOrder
        val maxCount = order.quantityOrder
        numberTextView.text = "${count.toString()}/${order.quantityOrder}"
        decreaseButton.setOnClickListener {
            if (count!! > 0) {
                count = count!! - 1
                numberTextView.text = "${count.toString()}/${order.quantityOrder}"
            }
        }

        increaseButton.setOnClickListener {
            if (count!! < maxCount!!) {
                count = count!! + 1
                numberTextView.text = "${count.toString()}/${order.quantityOrder}"
            }
        }


        btnSelesai.setOnClickListener {
            Log.d("aldi ","${order.id}")
            count?.let { it1 -> updateData(order.id!!, it1) }
            dialog.dismiss()

        }

        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()
    }

    private fun updateData(id: Int, quantityDeliv: Int) {
        val apiService = ApiConfig().getApiService()
        apiService.updateData(id, quantityDeliv)
            .enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    if (response.isSuccessful) {
                        val updateResponse = response.body()
                        updateResponse?.let {
                            val data = it.data
                            data?.let { data ->
                                Toast.makeText(this@PesananActivity, "Pesanan terupdate", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.d("aldo", "${response.code()}")
                        Toast.makeText(this@PesananActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    Toast.makeText(this@PesananActivity, "Failed to update makan", Toast.LENGTH_SHORT).show()
                }
            })
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