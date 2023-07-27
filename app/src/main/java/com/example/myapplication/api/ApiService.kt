package com.example.myapplication.api

import com.example.myapplication.data.OrderResponse
import com.example.myapplication.data.TableResponseItem
import com.example.myapplication.data.UpdateResponse
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

    @FormUrlEncoded
    @PATCH("updateQueue/{id}")
    fun updateData(
        @Path("id") id: Int,
        @Field("quantityDelivered") quantityDelivered:Int
    ) : Call<UpdateResponse>

    @GET("allTable")
    fun getAllTable(
    ): Call<List<TableResponseItem>>

}