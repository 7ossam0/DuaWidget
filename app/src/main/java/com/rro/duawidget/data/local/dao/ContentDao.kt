package com.rro.duawidget.data.local.dao

import androidx.room.*
import com.rro.duawidget.data.local.entity.ContentEntity
import com.rro.duawidget.domain.model.ContentType
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("""
        SELECT * FROM content_items 
        WHERE (:categoryId IS NULL OR categoryId = :categoryId)
          AND (:type IS NULL OR type = :type)
          AND (:onlyFavorites = 0 OR isFavorite = 1)
          AND (title LIKE '%' || :searchQuery || '%' OR body LIKE '%' || :searchQuery || '%')
        ORDER BY 
        CASE WHEN :sortBy = 'NEWEST' THEN createdAt END DESC,
        CASE WHEN :sortBy = 'OLDEST' THEN createdAt END ASC,
        CASE WHEN :sortBy = 'ALPHA' THEN title END ASC,
        CASE WHEN :sortBy = 'MANUAL' THEN sortOrder END ASC
    """)
    fun getFilteredContent(
        categoryId: Long?,
        type: ContentType?,
        onlyFavorites: Boolean,
        searchQuery: String,
        sortBy: String
    ): Flow<List<ContentEntity>>

    @Query("SELECT * FROM content_items WHERE (:categoryId IS NULL OR categoryId = :categoryId) AND (:onlyFavorites = 0 OR isFavorite = 1)")
    fun getForWidgetSync(categoryId: Long?, onlyFavorites: Boolean): List<ContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: ContentEntity): Long
    @Update
    suspend fun update(content: ContentEntity)
    @Delete
    suspend fun delete(content: ContentEntity)
}
