package studios.darkzen.dictionaryapp.common.core

import studios.darkzen.dictionaryapp.data.model.ApiErrorResponse

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: ApiErrorResponse) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}
