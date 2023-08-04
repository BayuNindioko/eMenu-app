package com.example.myapplication.data

data class DetailReservationResponse(
    val id: Int,
    val table_id: Int,
    val name: String,
    val pin: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val items: List<Any>,
    val table: TableInfo
)

data class TableInfo(
    val id: Int,
    val number: String,
    val status: String,
    val created_at: String?,
    val updated_at: String
)
