package com.example.guru_app_.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.guru_app_.BookDatabaseHelper
import com.example.guru_app_.models.Book

class BookDao(context: Context) {
    private val dbHelper: SQLiteOpenHelper = BookDatabaseHelper(context)

    fun addBook(book: Book) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", book.title)
            put("author", book.author)
            put("publisher", book.publisher)
            put("isbn", book.isbn)
            put("cover_image", book.coverImage)
            put("start_date", book.startDate)
            put("end_date", book.endDate)
            put("rating", book.rating)
            put("status", book.status)
        }
        db.insert("books", null, values)
        db.close()
    }

    fun getAllBooks(): List<Book> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("books", null, null, null, null, null, null)
        val books = mutableListOf<Book>()
        with(cursor) {
            while (moveToNext()) {
                val book = Book(
                    getInt(getColumnIndexOrThrow("id")),
                    getString(getColumnIndexOrThrow("title")),
                    getString(getColumnIndexOrThrow("author")),
                    getString(getColumnIndexOrThrow("publisher")),
                    getString(getColumnIndexOrThrow("isbn")),
                    getString(getColumnIndexOrThrow("cover_image")),
                    getString(getColumnIndexOrThrow("start_date")),
                    getString(getColumnIndexOrThrow("end_date")),
                    getFloat(getColumnIndexOrThrow("rating")),
                    getString(getColumnIndexOrThrow("status"))
                )
                books.add(book)
            }
        }
        cursor.close()
        db.close()
        return books
    }
}
