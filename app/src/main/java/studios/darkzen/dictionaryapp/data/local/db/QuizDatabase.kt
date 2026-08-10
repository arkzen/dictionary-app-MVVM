package studios.darkzen.dictionaryapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import studios.darkzen.dictionaryapp.data.local.dao.QuizDao
import studios.darkzen.dictionaryapp.data.local.entity.QuizAnswerEntity
import studios.darkzen.dictionaryapp.data.local.entity.QuizProgressEntity

@Database(
    entities = [QuizProgressEntity::class, QuizAnswerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}
