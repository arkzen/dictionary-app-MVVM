package studios.darkzen.dictionaryapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import studios.darkzen.dictionaryapp.data.local.dao.QuizDao
import studios.darkzen.dictionaryapp.data.local.db.QuizDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuizDatabase {
        return Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            "quiz_db"
        ).build()
    }

    @Provides
    fun provideQuizDao(database: QuizDatabase): QuizDao {
        return database.quizDao()
    }
}
