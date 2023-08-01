package com.example.myapplication.riwayat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.TableResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val _tableListLiveData = MutableLiveData<List<TableResponseItem>?>()
    val tableListLiveData: MutableLiveData<List<TableResponseItem>?> get() = _tableListLiveData

    fun loadTableData() {
        val apiService = ApiConfig().getApiService()
        apiService.getTable().enqueue(object : Callback<List<TableResponseItem>> {
            override fun onResponse(call: Call<List<TableResponseItem>>, response: Response<List<TableResponseItem>>) {
                if (response.isSuccessful) {
                    val tableList = response.body()
                    tableList?.let {
                        _tableListLiveData.postValue(tableList)
                    }
                } else {
                    // Handle error here
                }
            }

            override fun onFailure(call: Call<List<TableResponseItem>>, t: Throwable) {
                // Handle failure here
            }
        })
    }
}