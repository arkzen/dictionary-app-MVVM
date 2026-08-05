package studios.darkzen.dictionaryapp.data.model

import com.google.gson.JsonElement

data class ApiErrorResponse(
    val success: Boolean = false,
    val status_code: Int = -1,
    val message: JsonElement? = null
)
