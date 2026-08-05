package studios.darkzen.dictionaryapp.data.remote

import studios.darkzen.dictionaryapp.data.model.RootResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<RootResponse>
}
