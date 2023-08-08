package com.example.myapplication.data



data class OrderResponse(
	val id: Int,
	val table_id: Int,
	val name: String,
	val pin: String,
	val status: String,
	val created_at: String?,
	val updated_at: String?,
	val order_items: List<OrderItem>,
	val table: Table
)
data class OrderItem(
	val id: Int,
	val order_id: Int,
	val item_id: Int,
	val quantity_order: Int,
	val quantity_delivered: Int,
	val price: Int,
	val name: String,
	val notes: String?,
	val created_at: String?,
	val updated_at: String?,
	val laravel_through_key: Int,
	val item: Items
)

data class Items(
	val id: Int,
	val name: String,
	val deskripsi: String,
	val foto: String?,
	val number: String,
	val price: Int,
	val status: String,
	val created_at: String?,
	val updated_at: String?
)

data class Table(
	val id: Int,
	val number: String,
	val status: String,
	val created_at: String?,
	val updated_at: String?
)


