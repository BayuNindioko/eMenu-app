package com.example.myapplication.api

import com.example.myapplication.data.OrderResponse
import com.example.myapplication.data.TableResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @GET("table")
    fun getTable(
    ): Call<TableResponse>

    @GET("orderByTable")
    fun getOrderByTable(
    ): Call<OrderResponse>
}