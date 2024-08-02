package com.example.guru_app_.activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.guru_app_.R
import com.example.guru_app_.database.MemoDao
import com.example.guru_app_.models.Memo
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MemoDetailActivity : AppCompatActivity() {
    private var memoId: Int = -1
    private var bookId: Int = -1
    private lateinit var memoDao: MemoDao
    private lateinit var memoTitle: EditText
    private lateinit var memoContent: EditText
    private lateinit var addCameraImageButton: ImageButton
    private lateinit var addGalleryImageButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var imageView: ImageView

    private var currentPhotoPath: String? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)

        memoDao = MemoDao(this)

        memoId = intent.getIntExtra("MEMO_ID", -1)
        bookId = intent.getIntExtra("BOOK_ID", -1)

        memoTitle = findViewById(R.id.memo_title)
        memoContent = findViewById(R.id.memo_content)
        addCameraImageButton = findViewById(R.id.add_camera_image_button)
        addGalleryImageButton = findViewById(R.id.add_gallery_image_button)
        saveButton = findViewById(R.id.save_button)
        backButton = findViewById(R.id.back_button)
        imageView = findViewById(R.id.imageView)

        if (memoId != -1) {
            val memo: Memo? = memoDao.getMemoById(memoId)
            memo?.let {
                memoTitle.setText(it.title)
                memoContent.setText(it.content)
                if (it.imagePath != null) {
                    currentPhotoPath = it.imagePath
                    imageView.setImageURI(Uri.parse(it.imagePath))
                    imageView.visibility = View.VISIBLE
                }
            }
        }

        saveButton.setOnClickListener {
            saveMemo()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        addCameraImageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        addGalleryImageButton.setOnClickListener {
            dispatchPickPictureIntent()
        }
    }

    private fun saveMemo() {
        val title = memoTitle.text.toString()
        val content = memoContent.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.format(Date())

        if (memoId == -1) {
            val newMemo = Memo(
                bookId = bookId,
                title = title,
                content = content,
                page = null,
                imagePath = currentPhotoPath,
                createdAt = date,
                updatedAt = date
            )
            memoDao.addMemo(newMemo)
        } else {
            val updatedMemo = Memo(
                id = memoId,
                bookId = bookId,
                title = title,
                content = content,
                page = null,
                imagePath = currentPhotoPath,
                createdAt = date,
                updatedAt = date
            )
            memoDao.updateMemo(updatedMemo)
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.guru_app_.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchPickPictureIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    imageView.setImageBitmap(imageBitmap)
                    imageView.visibility = View.VISIBLE
                }
                REQUEST_IMAGE_PICK -> {
                    data?.data?.also { uri ->
                        currentPhotoPath = uri.toString()
                        imageView.setImageURI(uri)
                        imageView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
