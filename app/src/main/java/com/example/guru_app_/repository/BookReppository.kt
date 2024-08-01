package com.example.guru_app_.repository

import android.content.Context
import com.example.guru_app_.database.BookDao
import com.example.guru_app_.models.Book

class BookRepository(context: Context) {
    private val bookDao: BookDao = BookDao(context)

    fun addBook(book: Book) {
        bookDao.addBook(book)
    }

    fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks()
    }
}
