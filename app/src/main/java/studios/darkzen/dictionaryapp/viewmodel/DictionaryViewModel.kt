package studios.darkzen.dictionaryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import studios.darkzen.dictionaryapp.common.core.ResultState
import studios.darkzen.dictionaryapp.data.model.RootResponse
import studios.darkzen.dictionaryapp.data.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _dictionaryState = MutableStateFlow<ResultState<RootResponse>>(ResultState.Idle)
    val dictionaryState: StateFlow<ResultState<RootResponse>> = _dictionaryState.asStateFlow()

    fun getDefinition(word: String) {
        viewModelScope.launch {
            _dictionaryState.value = ResultState.Loading
            _dictionaryState.value = repository.getDefinition(word)
        }
    }
}
