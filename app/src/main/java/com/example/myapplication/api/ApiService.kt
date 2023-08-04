package com.example.myapplication.api

import com.example.myapplication.data.CheckinResponse
import com.example.myapplication.data.DetailReservationResponse
import com.example.myapplication.data.LoginResponse
import com.example.myapplication.data.LogoutResponse
import com.example.myapplication.data.OrderResponse
import com.example.myapplication.data.TableReservationResponse
import com.example.myapplication.data.TableResponseItem
import com.example.myapplication.data.UpdateResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/api/reservations")
    fun getTableReservation(
    ): Call<List<TableReservationResponse>>

    @GET("/api/reservations/{id}/items")
    fun getOrderByTable(
        @Path("id") number: String
    ): Call<OrderResponse>

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

    @POST("users/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("/api/tables/{table_id}/reservations")
    fun detailReservation(
        @Path("table_id") id: Int,
    ) : Call<DetailReservationResponse>

    @GET("/api/users/logout")
    fun logout(
        @Header("Authorization") token: String,
    ) : Call<LogoutResponse>

    @POST("/api/tables/{table_id}/reservations")
    @FormUrlEncoded
    fun checkIn(
        @Path("table_id") table_id: Int,
        @Field("name") name :String
    ): Call<CheckinResponse>

    @GET("/api/tables/{table_id}/reservations/{id}")
    fun checkOut(
        @Path("table_id") table_id: Int,
        @Path("id") id: Int,
    ): Call<DetailReservationResponse>
}