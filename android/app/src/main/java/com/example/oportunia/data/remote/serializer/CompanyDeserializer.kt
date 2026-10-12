package com.example.oportunia.data.remote.serializer




import com.example.oportunia.data.remote.dto.CompanyDto
import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.dto.SectorDto
import com.google.gson.*
import java.lang.reflect.Type
/**
 * Custom JSON deserializer for [CompanyDto] objects.
 *
 * Handles special cases like:
 * - Mixed id formats (string or numeric)
 * - Date parsing
 * - Properly deserializing nested objects
 */
class CompanyDeserializer : JsonDeserializer<CompanyDto> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CompanyDto  {
        val jsonObject = json.asJsonObject

        val id = try {
            jsonObject.get("id").asLong
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toLong()
        }

        val companyName = jsonObject.get("companyName")?.asString ?: "No Name"
        val website = jsonObject.get("website")?.asString ?: "No Website"
        val pictureUrl = jsonObject.get("pictureUrl")?.asString ?: "No PictureUrl"

        val internshipsJsonArray = jsonObject.get("internships")?.asJsonArray
        val internships = internshipsJsonArray?.map {
            context.deserialize<InternshipDto>(it, InternshipDto::class.java)
        } ?: emptyList()

        val sectorListJsonArray = jsonObject.get("sectorList")?.asJsonArray
        val sectors = sectorListJsonArray?.map {
            context.deserialize<SectorDto>(it, SectorDto::class.java)
        } ?: emptyList()

        return CompanyDto (
            id = id,
            name = companyName,
            website = website,
            pictureUrl = pictureUrl,
            internships = internships,
            sectorList = sectors
        )
    }
}
