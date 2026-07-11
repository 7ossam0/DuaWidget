package com.rro.duawidget.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rro.duawidget.data.local.dao.CategoryDao
import com.rro.duawidget.data.local.dao.ContentDao
import com.rro.duawidget.data.local.entity.CategoryEntity
import com.rro.duawidget.data.local.entity.ContentEntity
import com.rro.duawidget.domain.model.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CategoryEntity::class, ContentEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun contentDao(): ContentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dua_widget_db"
                )
                .addCallback(AppDatabaseCallback(context, scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val context: Context,
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(database: AppDatabase) {
            val defaultCategoryId = database.categoryDao().insert(
                CategoryEntity(name = "أذكار عامة")
            )
            database.contentDao().insert(
                ContentEntity(
                    title = "دعاء الغفران",
                    body = "اللهم اغفر لي وارحمني واهدني وعافني وارزقني",
                    type = ContentType.DUA,
                    categoryId = defaultCategoryId,
                    sortOrder = 0
                )
            )
        }
    }
}
