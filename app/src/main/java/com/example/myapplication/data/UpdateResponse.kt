package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class UpdateResponse(
	@field:SerializedName("data")
	val data: OrderData? = null
)

data class OrderData(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("item")
	val item: Item? = null,

	@field:SerializedName("quantity_order")
	val quantityOrder: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("notes")
	val notes: String? = null
)

data class Item(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)

