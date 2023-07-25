package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class TableResponse(

	@field:SerializedName("TableResponse")
	val tableResponse: List<TableResponseItem?>? = null
)

data class TableResponseItem(

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
