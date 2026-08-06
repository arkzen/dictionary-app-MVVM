package studios.darkzen.dictionaryapp.data.repository

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import studios.darkzen.dictionaryapp.data.model.QuizCategory
import studios.darkzen.dictionaryapp.data.model.QuizData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    @ApplicationContext private val context: Context
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
}
