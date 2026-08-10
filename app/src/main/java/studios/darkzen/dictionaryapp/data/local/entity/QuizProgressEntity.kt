package studios.darkzen.dictionaryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_progress")
data class QuizProgressEntity(
    @PrimaryKey val packId: String,
    val currentQuestionIndex: Int = 0,
    val answeredCount: Int = 0,
    val correctCount: Int = 0,
    val isCompleted: Boolean = false,
    val lastScore: Int = 0,
    val bestScore: Int = 0,
    val lastPlayedAt: Long = System.currentTimeMillis()
)
