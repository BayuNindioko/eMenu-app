package com.example.myapplication.data

data class Order(
    val orderId: Int,
    val tableId:String,
    val menuId: Int,
    val quantity:Int,
    val photo: Int,
    val name: String,
    val notes:String,
    val status: String,
    val created: String,
)