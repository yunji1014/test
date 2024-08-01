package com.example.guru_app_.models

data class Book(
    val id: Int? = null,  // ID 필드를 nullable로 설정
    val title: String,
    val author: String,
    val publisher: String,
    val isbn: String,
    val coverImage: String? = null, // coverImage 필드 추가
    val startDate: String? = null,
    val endDate: String? = null,
    val rating: Float? = null,
    val status: String = "reading"
)
