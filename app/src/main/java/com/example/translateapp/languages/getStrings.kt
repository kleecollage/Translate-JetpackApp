package com.example.translateapp.languages

import androidx.compose.runtime.Composable

@Composable
fun getStrings(): List<Map<String, String>> {
    val en = mapOf(
        "title" to "Hello World",
        "subtitle" to "The World Is Yours"
    )
    val es = mapOf(
        "title" to "Hola Mundo",
        "subtitle" to "El Mundo Es Tuyo"
    )

    return listOf(en, es)
}