package com.example.myapplication.pesanan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.Items
import com.example.myapplication.data.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananViewModel : ViewModel() {
    private val _itemsLiveData: MutableLiveData<List<Items>> = MutableLiveData()
    val itemsLiveData: LiveData<List<Items>> get() = _itemsLiveData

    fun loadPesananData(number: String) {
        val apiService = ApiConfig().getApiService()
        apiService.getOrderByTable(number).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(
                call: Call<OrderResponse>,
                response: Response<OrderResponse>
            ) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    orderResponse?.let { order ->
                        val itemsList = order.items
                        _itemsLiveData.postValue(itemsList)
                    }
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.e("bayu", "Error: ${t.message}", t)
            }
        })
    }
}