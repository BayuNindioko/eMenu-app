package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("item_id")
	val itemId: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("quantity_order")
	val quantityOrder: Int? = null,

	@field:SerializedName("quantity_delivered")
	val quantityDelivered: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("order_id")
	val orderId: Int? = null
)