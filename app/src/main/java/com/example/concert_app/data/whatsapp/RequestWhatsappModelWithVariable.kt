package com.example.concert_app.data.whatsapp

import com.google.gson.annotations.SerializedName

data class RequestWhatsappModelWithVariable(

	@field:SerializedName("template")
	val template: Template? = null,

	@field:SerializedName("messaging_product")
	val messagingProduct: String? = null,

	@field:SerializedName("to")
	val to: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Language(

	@field:SerializedName("code")
	val code: String? = null
)

data class Template(

	@field:SerializedName("components")
	val components: List<ComponentsItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("language")
	val language: Language? = null
)

data class ComponentsItem(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("parameters")
	val parameters: List<ParametersItem?>? = null
)

data class ParametersItem(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
