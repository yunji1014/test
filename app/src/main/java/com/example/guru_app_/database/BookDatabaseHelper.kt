package com.example.guru_app_.database
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "booklog.db"
        private const val DATABASE_VERSION = 1
        private const val CREATE_BOOKS_TABLE = """
            CREATE TABLE books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                publisher TEXT,
                isbn TEXT NOT NULL UNIQUE,
                cover_image TEXT,
                start_date TEXT,
                end_date TEXT,
                rating REAL,
                status TEXT NOT NULL DEFAULT 'reading'
            );
        """

        private const val CREATE_MEMOS_TABLE = """
            CREATE TABLE memos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id INTEGER NOT NULL,
                title TEXT,
                content TEXT,
                page INTEGER,
                image_path TEXT,
                created_at TEXT,
                updated_at TEXT,
                FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_BOOKS_TABLE)
        db.execSQL(CREATE_MEMOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 업그레이드 정책에 따라 필요한 경우에 데이터베이스 테이블 재구성
        db.execSQL("DROP TABLE IF EXISTS memos")
        db.execSQL("DROP TABLE IF EXISTS books")
        onCreate(db)
    }
}
