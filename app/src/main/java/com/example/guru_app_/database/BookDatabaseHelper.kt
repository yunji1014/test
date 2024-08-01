package com.example.guru_app_

import android.annotation.SuppressLint
import android.content.ContentValues
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

        private const val CREATE_STATISTICS_TABLE = """
            CREATE TABLE IF NOT EXISTS Statistics (
                user_id TEXT NOT NULL,
                month TEXT NOT NULL,
                books_read INTEGER DEFAULT 0,
                genre_stats TEXT,
                FOREIGN KEY (user_id) REFERENCES users(user_id),
                PRIMARY KEY (user_id, month)
            );
        """

        // books 테이블 컬럼 상수
        private const val TABLE_BOOKS = "books"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_IMAGE = "cover_image"
        private const val COLUMN_ISBN = "isbn"
        private const val COLUMN_PUBLISHER = "publisher"
        private const val COLUMN_STATUS = "status"

    }



    // onCreate 메서드는 데이터베이스가 처음 생성될 때 호출
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_BOOKS_TABLE)
        db.execSQL(CREATE_MEMOS_TABLE)
        db.execSQL(CREATE_STATISTICS_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 업그레이드 정책에 따라 필요한 경우에 데이터베이스 테이블 재구성
        db.execSQL("DROP TABLE IF EXISTS memos")
        db.execSQL("DROP TABLE IF EXISTS books")
        db.execSQL("DROP TABLE IF EXISTS Statistics")
        onCreate(db)
    }


    // addBook 메서드는 새로운 책 데이터를 데이터베이스에 추가
    fun addBook(book: Book) {
        val db = this.writableDatabase // 쓰기 가능한 데이터베이스 가져오기
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, book.title) // 제목 추가 .. 이하 같음
        contentValues.put(COLUMN_AUTHOR, book.author)
        contentValues.put(COLUMN_IMAGE, book.image)
        contentValues.put(COLUMN_ISBN, book.isbn)
        contentValues.put(COLUMN_PUBLISHER, book.publisher)
        contentValues.put(COLUMN_STATUS, book.status)

        db.insert("books", null, contentValues) // 데이터베이스에 데이터 삽입
        db.close() // 데이터베이스 닫기
    }

    // getAllBooks 메서드는 데이터베이스에서 모든 책 데이터를 조회하여 리스트로 반환
    @SuppressLint("Range")
    fun getAllBooks(): List<Book> {
        //ArrayList로 구현. 코틀린에서 ArrayList는 자동으로 배열값이 지정되기 때문에 유연한
        //개발 가능.
        val bookList: MutableList<Book> = ArrayList() // 책 리스트 초기화
        val selectQuery = "SELECT * FROM $TABLE_BOOKS"  // 모든 데이터를 선택하는 쿼리
        val db = this.readableDatabase // 읽기 가능한 데이터베이스 가져오기
        val cursor = db.rawQuery(selectQuery, null) // 쿼리 실행

        // 쿼리 결과가 있으면 리스트에 추가
        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ISBN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
                )
                bookList.add(book) // 리스트에 책 추가
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return bookList
    }


}
