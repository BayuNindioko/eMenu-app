package com.example.myapplication.riwayat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.TableResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val tableData: MutableLiveData<List<TableResponseItem>> = MutableLiveData()
    fun getTableData(): LiveData<List<TableResponseItem>> = tableData
    fun loadTableData() {
        val apiService = ApiConfig().getApiService()
        apiService.getTable().enqueue(object : Callback<List<TableResponseItem>> {
            override fun onResponse(call: Call<List<TableResponseItem>>, response: Response<List<TableResponseItem>>) {
                if (response.isSuccessful) {
                    val tableList = response.body()
                    tableList?.forEach { tableReservation ->
                        val tableList = response.body()
                        tableData.postValue(tableList)
                    }
                } else {
                    Log.d("aldo", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TableResponseItem>>, t: Throwable) {
                Log.e("aldo", "Error: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}