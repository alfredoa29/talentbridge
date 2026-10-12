package com.example.oportunia.data.remote.serializer

import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CompanyBasicDto
import com.example.oportunia.data.remote.dto.CompanyDto
import com.example.oportunia.data.remote.dto.InternshipDto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type



/**
 * Deserializer class for converting JSON data into an [InternshipDto] object.
 * This class handles optional fields with default values and supports nested deserialization
 * of the [CompanyDto] object if present.
 *
 * Example of expected JSON structure:
 * ```
 * {
 *   "id": 103,
 *   "title": "Diseñador UX/UI",
 *   "shortDescription": "Diseño de interfaces amigables",
 *   "description": "Participación en diseño de interfaces...",
 *   "logoUrl": "https://example.com/logo.png",
 *   "companyDto": {
 *     "id": 20,
 *     "companyName": "InnovaDesign",
 *     "website": "https://innovadesign.com"
 *   }
 * }
 * ```
 */
class InternshipDeserializer : JsonDeserializer<InternshipDto> {

    /**
     * Deserializes JSON data into an [InternshipDto] object.
     *
     * @param json The JSON element to deserialize
     * @param typeOfT The type of the object to deserialize
     * @param context The deserialization context
     * @return A fully populated [InternshipDto] object
     * @throws JsonParseException if there's an error during parsing
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): InternshipDto {
        val jsonObject = json.asJsonObject

        // Extract the ID, handling both Long and String formats
        val id = try {
            jsonObject["id"].asLong
        } catch (e: Exception) {
            jsonObject["id"]?.asString?.toLongOrNull() ?: 0L
        }

        // Extract primitive fields with null safety
        val title = jsonObject["title"]?.asString ?: "No Title"
        val shortDescription = jsonObject["shortDescription"]?.asString ?: "No Description"
        val description = jsonObject["description"]?.asString ?: "No Details"
        val active = jsonObject["active"]?.asBoolean ?: false

        // Deserialize nested companyBasicDto instead of companyDto
        val companyBasicDto = jsonObject["company"].let {
            context.deserialize<CompanyBasicDto>(it, CompanyBasicDto::class.java)
        }

        // Deserialize nested AreaOfInterrest
        val areaOfInterestDto = jsonObject["category"].let {
            context.deserialize<AreaOfInterestDto>(it, AreaOfInterestDto::class.java)
        }

        // Return fully populated InternshipDto
        return InternshipDto(
            id = id,
            title = title,
            shortDescription = shortDescription,
            active = active,
            description = description,
            company = companyBasicDto,
            category = areaOfInterestDto
        )
    }
}
