package com.example.solveit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.solveit.model.Score


@Database(
    entities = [Score::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ScoreDatabase : RoomDatabase() {

    abstract fun getScoreDao(): ScoreDao

    companion object {
        @Volatile
        private var instance: ScoreDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ScoreDatabase::class.java,
                "score_db.db"
            ).build()
    }
}