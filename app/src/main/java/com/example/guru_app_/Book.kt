package com.example.guru_app_

import org.w3c.dom.Text

data class Book(
    val title: String,
    val author: String,
    val image: String,
    val isbn: String,
    val publisher: String,
    val status: String  // status 필드 추가
)
