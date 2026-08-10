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
import studios.darkzen.dictionaryapp.data.local.prefs.QuizPreferenceManager
import studios.darkzen.dictionaryapp.data.model.QuizCategory
import studios.darkzen.dictionaryapp.data.model.QuizPack
import studios.darkzen.dictionaryapp.data.repository.QuizRepository
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val prefManager: QuizPreferenceManager
) : ViewModel() {

    private val _categories = MutableStateFlow<List<QuizCategory>>(emptyList())
    val categories: StateFlow<List<QuizCategory>> = _categories.asStateFlow()

    private val _dailyQuiz = MutableStateFlow<TodayQuizState?>(null)
    val dailyQuiz: StateFlow<TodayQuizState?> = _dailyQuiz.asStateFlow()

    val allProgress: StateFlow<List<QuizProgressEntity>> = repository.getAllProgress()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val loadedCategories = repository.getQuizCategories()
            _categories.value = loadedCategories
            
            // Wait for initial progress to be ready
            val progressList = allProgress.value
            selectDailyQuiz(loadedCategories, progressList)
            
            observeProgressForDailyQuiz()
        }
    }

    private fun observeProgressForDailyQuiz() {
        viewModelScope.launch {
            allProgress.collect { progressList ->
                val currentDaily = _dailyQuiz.value
                if (currentDaily != null) {
                    val updatedProgress = progressList.find { it.packId == currentDaily.pack.id }
                    _dailyQuiz.value = currentDaily.copy(progress = updatedProgress)
                }
            }
        }
    }

    private fun selectDailyQuiz(categories: List<QuizCategory>, progressList: List<QuizProgressEntity>) {
        if (categories.isEmpty()) return

        val allPacks = categories.flatMap { cat -> cat.packs.map { it to cat.name } }
        if (allPacks.isEmpty()) return

        val storedPackId = prefManager.getDailyQuizPackId()
        var selectedPackWithCat = allPacks.find { it.first.id == storedPackId }

        if (selectedPackWithCat == null) {
            // New day or no stored quiz, select one
            val previousPackId = prefManager.getPreviousDailyQuizPackId()
            
            var candidates = allPacks.filter { (pack, _) ->
                val p = progressList.find { it.packId == pack.id }
                p == null || !p.isCompleted
            }

            if (candidates.isEmpty()) candidates = allPacks

            // Avoid previous day's pack if possible
            if (candidates.size > 1 && previousPackId != null) {
                val filtered = candidates.filter { it.first.id != previousPackId }
                if (filtered.isNotEmpty()) candidates = filtered
            }

            // Select deterministically for the day
            val dayHash = prefManager.getTodayDate().hashCode()
            selectedPackWithCat = candidates[Math.abs(dayHash) % candidates.size]
            
            prefManager.saveDailyQuiz(selectedPackWithCat.first.id)
        }

        val progress = progressList.find { it.packId == selectedPackWithCat.first.id }
        _dailyQuiz.value = TodayQuizState(selectedPackWithCat.first, selectedPackWithCat.second, progress)
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

data class TodayQuizState(
    val pack: QuizPack,
    val categoryName: String,
    val progress: QuizProgressEntity?
)
