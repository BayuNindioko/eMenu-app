package com.example.myapplication.data

data class TableReservationResponse(
    val id: Int,
    val table_id: Int,
    val name: String,
    val pin: String,
    val status: String,
    val created_at: String?,
    val updated_at: String?,
    val table: TableRes
)

data class TableRes(
    val id: Int,
    val number: String?,
    val status: String,
    val created_at: String?,
    val updated_at: String?
)
