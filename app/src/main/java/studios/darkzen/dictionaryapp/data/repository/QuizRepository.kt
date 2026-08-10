package studios.darkzen.dictionaryapp.data.repository

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import studios.darkzen.dictionaryapp.data.local.dao.QuizDao
import studios.darkzen.dictionaryapp.data.local.entity.QuizAnswerEntity
import studios.darkzen.dictionaryapp.data.local.entity.QuizProgressEntity
import studios.darkzen.dictionaryapp.data.model.QuizCategory
import studios.darkzen.dictionaryapp.data.model.QuizData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val quizDao: QuizDao
) {
    private val gson = Gson()

    fun getQuizCategories(): List<QuizCategory> {
        return try {
            val jsonString = context.assets.open("quizzes.json").bufferedReader().use { it.readText() }
            val quizData = gson.fromJson(jsonString, QuizData::class.java)
            quizData.categories
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getQuotes(): List<studios.darkzen.dictionaryapp.data.model.Quote> {
        return try {
            val jsonString = context.assets.open("quotes.json").bufferedReader().use { it.readText() }
            val quoteData = gson.fromJson(jsonString, studios.darkzen.dictionaryapp.data.model.QuoteData::class.java)
            quoteData.quotes
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProgressById(packId: String) = quizDao.getProgressById(packId)

    fun getAllProgress(): Flow<List<QuizProgressEntity>> = quizDao.getAllProgress()

    suspend fun saveProgress(progress: QuizProgressEntity) = quizDao.saveProgress(progress)

    suspend fun saveAnswer(answer: QuizAnswerEntity) = quizDao.saveAnswer(answer)

    suspend fun resetPackProgress(packId: String, currentScore: Int) {
        quizDao.deleteAnswersByPackId(packId)
        val currentProgress = quizDao.getProgressById(packId)
        quizDao.saveProgress(QuizProgressEntity(
            packId = packId,
            bestScore = maxOf(currentScore, currentProgress?.bestScore ?: 0),
            isCompleted = false,
            currentQuestionIndex = 0,
            answeredCount = 0,
            correctCount = 0
        ))
    }
}
