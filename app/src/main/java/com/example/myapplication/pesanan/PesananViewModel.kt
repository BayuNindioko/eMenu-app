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
        apiService.getOrderByTable(number).enqueue(object : Callback<List<OrderResponse>> {
            override fun onResponse(
                call: Call<List<OrderResponse>>,
                response: Response<List<OrderResponse>>
            ) {
                if (response.isSuccessful) {
                    val orderList = response.body()
                    orderList?.let { orders ->
                        if (orders.isNotEmpty()) {
                            val itemsList = orders[0].items
                            _itemsLiveData.postValue(itemsList)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                Log.d("bayu","failed")
            }
        })
    }
}