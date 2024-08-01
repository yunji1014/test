package com.example.guru_app_.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.guru_app_.R
import com.example.guru_app_.database.BookDao
import com.example.guru_app_.database.MemoDao
import com.example.guru_app_.fragments.CompletedBookDetailFragment
import com.example.guru_app_.fragments.MemoListFragment
import com.example.guru_app_.fragments.ReadingBookDetailFragment
import com.example.guru_app_.models.Book
import com.example.guru_app_.models.Memo
import java.text.SimpleDateFormat
import java.util.*

class BookMemoActivity : AppCompatActivity(), MemoListFragment.MemoItemClickListener {

    private lateinit var memoDao: MemoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_memo)

        val bookDao = BookDao(this)
        memoDao = MemoDao(this)

        // 테스트 데이터를 추가합니다
        val testBook = Book(
            title = "테스트 책 제목",
            author = "테스트 저자",
            publisher = "테스트 출판사",
            isbn = "1234567890",
            coverImage = null,
            startDate = "2022-01-01",
            endDate = null,
            rating = null,
            status = "reading"
        )
        bookDao.addBook(testBook)

        // 데이터베이스에서 모든 책을 조회합니다
        val books = bookDao.getAllBooks()
        for (book in books) {
            Log.d("BookMemoActivity", "Book: ${book.title}, Author: ${book.author}")
        }

        val isCompleted = intent.getBooleanExtra("IS_COMPLETED", false)

        if (savedInstanceState == null) {
            if (isCompleted) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.book_detail_fragment_container, CompletedBookDetailFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.book_detail_fragment_container, ReadingBookDetailFragment())
                    .commit()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.memo_list_fragment_container, MemoListFragment())
                .commit()
        }

        findViewById<ImageButton>(R.id.imageButton2).setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = dateFormat.format(Date())

            val newMemo = Memo(
                bookId = 1,  // 테스트를 위해 임의의 bookId 1 사용
                title = "New Memo",
                content = "",
                page = null,
                imagePath = null,
                createdAt = date,
                updatedAt = null
            )
            memoDao.addMemo(newMemo)

            val intent = Intent(this, MemoDetailActivity::class.java)
            intent.putExtra("MEMO_ID", newMemo.id ?: -1)
            startActivityForResult(intent, REQUEST_CODE_MEMO_DETAIL)
        }
    }

    override fun onMemoItemClick(memoId: Int) {
        val intent = Intent(this, MemoDetailActivity::class.java)
        intent.putExtra("MEMO_ID", memoId)
        startActivityForResult(intent, REQUEST_CODE_MEMO_DETAIL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MEMO_DETAIL && resultCode == Activity.RESULT_OK) {
            val memoListFragment = supportFragmentManager.findFragmentById(R.id.memo_list_fragment_container) as? MemoListFragment
            memoListFragment?.refreshMemoList()
        }
    }

    companion object {
        private const val REQUEST_CODE_MEMO_DETAIL = 1
    }
}
