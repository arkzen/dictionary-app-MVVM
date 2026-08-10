package studios.darkzen.dictionaryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import studios.darkzen.dictionaryapp.data.local.entity.QuizAnswerEntity
import studios.darkzen.dictionaryapp.data.local.entity.QuizProgressEntity
import studios.darkzen.dictionaryapp.data.model.QuizCategory
import studios.darkzen.dictionaryapp.data.model.QuizPack
import studios.darkzen.dictionaryapp.data.repository.QuizRepository
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<QuizCategory>>(emptyList())
    val categories: StateFlow<List<QuizCategory>> = _categories.asStateFlow()

    val allProgress: StateFlow<List<QuizProgressEntity>> = repository.getAllProgress()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getQuizCategories()
        }
    }

    fun getCategoryById(categoryId: String): QuizCategory? {
        return _categories.value.find { it.id == categoryId }
    }

    fun getPackById(packId: String): QuizPack? {
        return _categories.value.flatMap { it.packs }.find { it.id == packId }
    }

    suspend fun getProgressById(packId: String) = repository.getProgressById(packId)

    fun saveProgress(progress: QuizProgressEntity) {
        viewModelScope.launch {
            repository.saveProgress(progress)
        }
    }

    fun saveAnswer(answer: QuizAnswerEntity) {
        viewModelScope.launch {
            repository.saveAnswer(answer)
        }
    }

    suspend fun resetPackProgress(packId: String, currentBestScore: Int) {
        repository.resetPackProgress(packId, currentBestScore)
    }
}
