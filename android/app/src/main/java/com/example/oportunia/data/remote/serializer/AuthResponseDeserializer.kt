package com.example.oportunia.data.remote.serializer

import com.example.oportunia.data.remote.dto.AuthResponseDto
import com.example.oportunia.data.remote.dto.AuthorityDto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

import com.google.gson.*

class AuthResponseDeserializer : JsonDeserializer<AuthResponseDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: java.lang.reflect.Type?,
        context: JsonDeserializationContext?
    ): AuthResponseDto {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("JSON is null")
        val token = jsonObject.get("token")?.asString ?: ""
        val username = jsonObject.get("username")?.asString ?: ""

        val authoritiesJsonArray = jsonObject.getAsJsonArray("authorities") ?: JsonArray()

        // Deserializar la lista de AuthorityDto usando context para que Gson lo haga bien
        val authorities = authoritiesJsonArray.map { element ->
            context?.deserialize<AuthorityDto>(element, AuthorityDto::class.java)
                ?: throw JsonParseException("Failed to deserialize AuthorityDto")
        }

        return AuthResponseDto(
            token = token,
            username = username,
            authorities = authorities
        )
    }
}
