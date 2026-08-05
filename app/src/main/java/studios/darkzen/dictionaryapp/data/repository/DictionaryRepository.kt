package studios.darkzen.dictionaryapp.data.repository

import studios.darkzen.dictionaryapp.common.core.BaseRepository
import studios.darkzen.dictionaryapp.common.core.ResultState
import studios.darkzen.dictionaryapp.data.model.RootResponse
import studios.darkzen.dictionaryapp.data.remote.DictionaryApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepository @Inject constructor(
    private val api: DictionaryApi
) : BaseRepository() {

    suspend fun getDefinition(word: String): ResultState<RootResponse> {
        return safeApiCall {
            api.getDefinition(word).first()
        }
    }
}
