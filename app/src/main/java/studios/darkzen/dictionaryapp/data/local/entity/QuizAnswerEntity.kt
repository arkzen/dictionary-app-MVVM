package studios.darkzen.dictionaryapp.data.local.entity

import androidx.room.Entity

@Entity(tableName = "quiz_answers", primaryKeys = ["packId", "questionId"])
data class QuizAnswerEntity(
    val packId: String,
    val questionId: Int,
    val selectedAnswerIndex: Int,
    val isCorrect: Boolean
)
