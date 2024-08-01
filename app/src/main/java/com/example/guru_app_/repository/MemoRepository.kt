package com.example.guru_app_.repository

import android.content.Context
import com.example.guru_app_.database.MemoDao
import com.example.guru_app_.models.Memo

class MemoRepository(context: Context) {
    private val memoDao: MemoDao = MemoDao(context)

    fun addMemo(memo: Memo) {
        memoDao.addMemo(memo)
    }

    fun getMemosForBook(bookId: Int): List<Memo> {
        return memoDao.getMemosForBook(bookId)
    }

    fun getMemoById(memoId: Int): Memo? {
        return memoDao.getMemoById(memoId)
    }
}
