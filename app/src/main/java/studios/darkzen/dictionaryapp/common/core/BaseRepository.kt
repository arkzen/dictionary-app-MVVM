package studios.darkzen.dictionaryapp.common.core

import studios.darkzen.dictionaryapp.data.model.ApiErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRepository {

    private val gson = Gson()

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultState<T> {
        return try {
            ResultState.Success(apiCall())
        } catch (e: HttpException) {
            val code = e.code()
            val bodyString = e.response()?.errorBody()?.string()

            val parsed = try {
                gson.fromJson(bodyString, ApiErrorResponse::class.java)
                    ?: ApiErrorResponse(message = null)
            } catch (_: Exception) {
                ApiErrorResponse(message = null)
            }

            ResultState.Error(
                parsed.copy(
                    success = false,
                    status_code = if (parsed.status_code != -1) parsed.status_code else code
                )
            )
        } catch (e: IOException) {
            ResultState.Error(
                ApiErrorResponse(
                    success = false,
                    status_code = -1,
                    message = gson.toJsonTree("No internet connection")
                )
            )
        } catch (e: Exception) {
            ResultState.Error(
                ApiErrorResponse(
                    success = false,
                    status_code = -1,
                    message = gson.toJsonTree(e.localizedMessage ?: "An unknown error occurred")
                )
            )
        }
    }
}
