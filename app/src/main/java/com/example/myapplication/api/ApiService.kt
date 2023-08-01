package com.example.myapplication.api

import com.example.myapplication.data.OrderResponse
import com.example.myapplication.data.TableResponseItem
import com.example.myapplication.data.UpdateResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("tables")
    fun getTableReservation(
    ): Call<List<TableResponseItem>>
    @GET("/reservations/{id}/items")
    fun getOrderByTable(
        @Path("table_id") number: String
    ): Call<List<OrderResponse>>

    @FormUrlEncoded
    @PATCH("order_items/{id}/")
    fun updateData(
        @Path("id") id: Int,
        @Field("quantity_delivered") QuantityDelivered:Int
    ) : Call<UpdateResponse>

    @GET("tables")
    fun getTable(
    ): Call<List<TableResponseItem>>
    @GET("tables/{table_id}")
    fun getHistoryByTable(
        @Path("table_id") number: String
    ): Call<List<OrderResponse>>

}