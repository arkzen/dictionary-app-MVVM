package studios.darkzen.dictionaryapp.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import studios.darkzen.dictionaryapp.data.local.entity.QuizAnswerEntity
import studios.darkzen.dictionaryapp.data.local.entity.QuizProgressEntity

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_progress WHERE packId = :packId")
    suspend fun getProgressById(packId: String): QuizProgressEntity?

    @Query("SELECT * FROM quiz_progress")
    fun getAllProgress(): Flow<List<QuizProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: QuizProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAnswer(answer: QuizAnswerEntity)

    @Query("SELECT * FROM quiz_answers WHERE packId = :packId")
    suspend fun getAnswersByPackId(packId: String): List<QuizAnswerEntity>

    @Query("DELETE FROM quiz_answers WHERE packId = :packId")
    suspend fun deleteAnswersByPackId(packId: String)
}
