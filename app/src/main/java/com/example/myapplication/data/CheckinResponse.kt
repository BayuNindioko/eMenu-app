package com.example.myapplication.data

data class CheckinResponse (
    val name: String,
    val table_id: Int?=null,
    val pin: String,
    val status: String,
    val updated_at: String,
    val created_at: String,
    val id: Int

)