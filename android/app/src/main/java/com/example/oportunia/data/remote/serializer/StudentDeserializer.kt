package com.example.oportunia.data.remote.serializer

import android.util.Log
import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CompanyBasicDto
import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.data.remote.dto.GenderDto
import com.example.oportunia.data.remote.dto.SkillDto
import com.example.oportunia.data.remote.dto.StudentDto
import com.google.gson.*
import java.lang.reflect.Type
/**
 * Custom JSON deserializer for [StudentDto] objects.
 *
 * Handles special cases like:
 * - Mixed id formats (string or numeric)
 * - Date parsing
 * - Properly deserializing nested objects
 */
class StudentDeserializer : JsonDeserializer<StudentDto> {

    /**
     * Deserializes JSON data into a [StudentDto] object.
     *
     * @param json The JSON element to deserialize
     * @param typeOfT The type of the object to deserialize
     * @param context The deserialization context
     * @return A fully populated [StudentDto] object
     * @throws JsonParseException if there's an error during parsing
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): StudentDto {
        val jsonObject = json.asJsonObject

        // Extracts the id, handling both integer and string formats
        val id = try {
            jsonObject.get("id").asLong
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toLong()
        }

        // Extract other fields with null safety and assign default values
        val fullName = jsonObject.get("fullName")?.asString ?: "No Full Name"
        val university = jsonObject.get("university")?.asString ?: "No University"
        val career = jsonObject.get("career")?.asString ?: "No Career"
        val profilePicUrl = jsonObject.get("profilePicUrl")?.asString?: "No ProfilePicUrl"
        val username = jsonObject.get("username")?.asString?: "No username"
        val email = jsonObject.get("email")?.asString?: "No email"


        // Deserialize the skills list (assuming it's a list of SkillDto objects)
        val skillsJsonArray = jsonObject.get("skillList")?.asJsonArray
        val skills = skillsJsonArray?.map {
            context.deserialize<SkillDto>(it, SkillDto::class.java)
        } ?: emptyList()

        val areaOfInterestArray = jsonObject.get("areaOfInterestList")?.asJsonArray
        val areaOfInterests = areaOfInterestArray?.map {
            context.deserialize<AreaOfInterestDto> (it, AreaOfInterestDto::class.java)
        } ?: emptyList()



        val country = jsonObject.get("country").let {
            context.deserialize<CountryDto>(it, CountryDto::class.java)
        }

        val gender = jsonObject.get("gender").let {
            context.deserialize<GenderDto>(it, GenderDto::class.java)
        }


        // Return the fully populated StudentDto object without internships
        return StudentDto(id, fullName, university, career,username, email, skills, areaOfInterests, profilePicUrl, country, gender)
    }
}
