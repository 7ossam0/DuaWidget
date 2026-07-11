package com.rro.duawidget.data.local

import androidx.room.TypeConverter
import com.rro.duawidget.domain.model.ContentType

class Converters {
    @TypeConverter
    fun toContentType(value: String) = enumValueOf<ContentType>(value)
    @TypeConverter
    fun fromContentType(value: ContentType) = value.name
}
