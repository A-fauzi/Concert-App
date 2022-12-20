package com.example.concert_app.data.whatsapp

import com.google.gson.annotations.SerializedName

data class WhatsappModel(

	@field:SerializedName("messaging_product")
	val messagingProduct: String? = null,

	@field:SerializedName("messages")
	val messages: List<MessagesItem?>? = null,

	@field:SerializedName("contacts")
	val contacts: List<ContactsItem?>? = null
)

data class MessagesItem(

	@field:SerializedName("id")
	val id: String? = null
)

data class ContactsItem(

	@field:SerializedName("input")
	val input: String? = null,

	@field:SerializedName("wa_id")
	val waId: String? = null
)
