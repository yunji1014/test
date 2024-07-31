package com.example.guru_app_

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // companion object는 상수 정의 및 정적 메서드를 포함하는 객체
    companion object {
        private const val DATABASE_NAME = "books.db" // 데이터베이스 이름
        private const val DATABASE_VERSION = 1
        private const val TABLE_BOOKS = "books" //테이블 이름
        private const val COLUMN_BOOK_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_ISBN = "isbn"
        private const val COLUMN_PUBLISHER = "publisher"
        private const val COLUMN_START = "start_date"
        private const val COLUMN_END = "end_date"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_STATUS = "status"

        private const val TABLE_MEMOS = "memos"
        private const val COLUMN_MEMO_ID = "id"
        private const val COLUMN_BOOK_ID_FK = "book_id"
        private const val COLUMN_MEMO_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_PAGE = "page"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_CREATED_AT = "created_at"
        private const val COLUMN_UPDATED_AT = "updated_at"

        private const val CREATE_BOOKS_TABLE = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_BOOK_ID INTEGER,
                $COLUMN_TITLE TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_IMAGE TEXT,
                $COLUMN_ISBN TEXT,
                $COLUMN_PUBLISHER TEXT,
                $COLUMN_START TEXT,
                $COLUMN_END TEXT,
                $COLUMN_RATING REAL,
                $COLUMN_STATUS TEXT NOT NULL DEFAULT 'reading'
            );
        """

        private const val CREATE_MEMOS_TABLE = """
            CREATE TABLE $TABLE_MEMOS (
                $COLUMN_MEMO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BOOK_ID_FK INTEGER NOT NULL,
                $COLUMN_MEMO_TITLE TEXT,
                $COLUMN_CONTENT TEXT,
                $COLUMN_PAGE INTEGER,
                $COLUMN_IMAGE_PATH TEXT,
                $COLUMN_CREATED_AT TEXT,
                $COLUMN_UPDATED_AT TEXT,
                FOREIGN KEY ($COLUMN_BOOK_ID_FK) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID) ON DELETE CASCADE
            );
        """

    }

    // onCreate 메서드는 데이터베이스가 처음 생성될 때 호출
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_BOOKS_TABLE)
        db.execSQL(CREATE_MEMOS_TABLE)
    }

    // onUpgrade 메서드는 데이터베이스가 업그레이드될 때 호출
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
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

        db.insert(TABLE_BOOKS, null, contentValues) // 데이터베이스에 데이터 삽입
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
                    cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHER))
                )
                bookList.add(book) // 리스트에 책 추가
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return bookList
    }
}