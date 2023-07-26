package com.example.myapplication.api

import com.example.myapplication.data.OrderResponse
import com.example.myapplication.data.TableResponseItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("table")
    fun getTable(
    ): Call<List<TableResponseItem>>

    @GET("orderByTable/{number}")
    fun getOrderByTable(
        @Path("number") number: String
    ): Call<List<OrderResponse>>
}