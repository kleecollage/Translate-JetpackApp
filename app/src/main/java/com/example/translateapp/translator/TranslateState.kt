package com.example.translateapp.translator

data class TranslateState(
    val textToTanslate: String = "",
    val translateText: String = "",
    val isDownloading: Boolean = false
)
