package com.example.guru_app_.database
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.guru_app_.Book

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
        private const val COLUMN_CATEGORY = "category" // 새로운 컬럼 추가

    }

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

    // 책 추가 메서드
    fun addBook(book: Book) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, book.title)
            put(COLUMN_AUTHOR, book.author)
            put(COLUMN_IMAGE, book.image)
            put(COLUMN_ISBN, book.isbn)
            put(COLUMN_PUBLISHER, book.publisher)
            put(COLUMN_CATEGORY, book.category) // 새로운 컬럼 추가
        }
    // 모든 책 데이터 조회 메서드
    @SuppressLint("Range")
    fun getAllBooks(): List<Book> {
        val bookList: MutableList<Book> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_BOOKS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ISBN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
                )
                bookList.add(book)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return bookList
    }


}
