package com.example.guru_app_.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.guru_app_.R
import com.example.guru_app_.database.MemoDao
import com.example.guru_app_.models.Memo
import java.text.SimpleDateFormat
import java.util.*

class MemoDetailActivity : AppCompatActivity() {
    private var memoId: Int = -1
    private lateinit var memoDao: MemoDao
    private lateinit var memoTitle: EditText
    private lateinit var memoContent: EditText
    private lateinit var addCameraImageButton: ImageButton
    private lateinit var addGalleryImageButton: ImageButton
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)

        memoDao = MemoDao(this)

        memoId = intent.getIntExtra("MEMO_ID", -1)

        memoTitle = findViewById(R.id.memo_title)
        memoContent = findViewById(R.id.memo_content)
        addCameraImageButton = findViewById(R.id.add_camera_image_button)
        addGalleryImageButton = findViewById(R.id.add_gallery_image_button)
        saveButton = findViewById(R.id.save_button)

        if (memoId != -1) {
            val memo: Memo? = memoDao.getMemoById(memoId)
            memo?.let {
                memoTitle.setText(it.title)
                memoContent.setText(it.content)
            }
        }

        saveButton.setOnClickListener {
            saveMemo()
        }

        // TODO: Add functionality for camera and gallery image buttons
    }

    private fun saveMemo() {
        val title = memoTitle.text.toString()
        val content = memoContent.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.format(Date())

        if (memoId == -1) {
            // 새로운 메모 추가
            val newMemo = Memo(
                bookId = 1,  // 테스트를 위해 임의의 bookId 1 사용
                title = title,
                content = content,
                page = null,
                imagePath = null,
                createdAt = date,
                updatedAt = date
            )
            memoDao.addMemo(newMemo)
        } else {
            // 기존 메모 업데이트
            val updatedMemo = Memo(
                id = memoId,
                bookId = 1,  // 테스트를 위해 임의의 bookId 1 사용
                title = title,
                content = content,
                page = null,
                imagePath = null,
                createdAt = date,
                updatedAt = date
            )
            memoDao.updateMemo(updatedMemo)
        }

        setResult(Activity.RESULT_OK)
        finish()
    }
}
