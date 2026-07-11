package com.rro.duawidget.domain.model

enum class ContentType {
    DUA, HADITH, AYAH
}

data class AyahDetails(
    val surahName: String,
    val ayahNumber: Int,
    val tafsir: String? = null
) {
    init {
        require(surahName.isNotBlank()) { "اسم السورة مطلوب" }
        require(ayahNumber > 0) { "رقم الآية يجب أن يكون أكبر من الصفر" }
    }
}
