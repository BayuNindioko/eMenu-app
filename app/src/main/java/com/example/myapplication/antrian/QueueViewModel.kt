package com.example.myapplication.antrian

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.TableResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueueViewModel : ViewModel() {
    private val apiService = ApiConfig().getApiService()
    private val tableData: MutableLiveData<List<TableResponseItem>> = MutableLiveData()
    fun getTableData(): LiveData<List<TableResponseItem>> = tableData


    fun fetchTableData() {
        apiService.getTable().enqueue(object : Callback<List<TableResponseItem>> {
            override fun onResponse(call: Call<List<TableResponseItem>>, response: Response<List<TableResponseItem>>) {
                if (response.isSuccessful) {
                    val tableList = response.body()
                    tableList?.let {
                        val filteredTableList = it.filter { table -> table.status == "Berisi" }
                        tableData.postValue(filteredTableList)
                    }
                }
            }

            override fun onFailure(call: Call<List<TableResponseItem>>, t: Throwable) {
                Log.e("aldo", "Error: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}

