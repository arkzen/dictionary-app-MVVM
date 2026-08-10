package studios.darkzen.dictionaryapp.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizPreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)

    fun getTodayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun getDailyQuizPackId(): String? {
        val storedDate = prefs.getString("daily_quiz_date", null)
        if (storedDate == getTodayDate()) {
            return prefs.getString("daily_quiz_pack_id", null)
        }
        return null
    }

    fun saveDailyQuiz(packId: String) {
        val currentDaily = prefs.getString("daily_quiz_pack_id", null)
        prefs.edit().apply {
            putString("daily_quiz_date", getTodayDate())
            putString("daily_quiz_pack_id", packId)
            if (currentDaily != null && currentDaily != packId) {
                putString("previous_daily_quiz_pack_id", currentDaily)
            }
            apply()
        }
    }

    fun getPreviousDailyQuizPackId(): String? {
        return prefs.getString("previous_daily_quiz_pack_id", null)
    }
}
