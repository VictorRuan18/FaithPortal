package com.example.faithportal.model

data class BibleVerse(
    var book: String,
    var chapter: Int,
    var verse: Int,
    val text: String
)