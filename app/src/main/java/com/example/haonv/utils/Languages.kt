package com.example.haonv.utils

object Languages {
    private val languageMap = mapOf(
        "vi" to "Vietnamese",
        "en" to "English",
        "ko" to "Korean",
        "fr" to "French"
    )

    fun getLanguageName(code: String): String {
        return languageMap[code] ?: "English"
    }

    fun getLanguageCode(name: String): String {
        return languageMap.entries.find { it.value == name }?.key ?: "en"
    }

}