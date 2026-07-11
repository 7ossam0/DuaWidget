package com.rro.duawidget.data.local.dao

import androidx.room.*
import com.rro.duawidget.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: CategoryEntity): Long
    @Update
    suspend fun update(category: CategoryEntity)
    @Delete
    suspend fun delete(category: CategoryEntity)
}
