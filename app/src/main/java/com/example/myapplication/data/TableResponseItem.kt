package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class TableResponseItem(
	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("number")
	val number: String? = null,

	@SerializedName("status")
	val status: String? = null,

	@SerializedName("created_at")
	val createdAt: Any? = null,

	@SerializedName("updated_at")
	val updatedAt: Any? = null
)
