package com.example.guru_app_

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide


class ReadingFragment : Fragment() {
//    lateinit var dbManager: DBManager
    lateinit var DB: SQLiteDatabase
    lateinit var layout: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading, container, false)
        layout = requireView().findViewById(R.id.books)

//        dbManager = DBManager(this, "DB이름", null, 1)
//        DB = dbManager.readableDatabase
        loadBooks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8fA%3D%3D"
        val image1 = view.findViewById<ImageView>(R.id.book_cover)
        Glide.with(this)
            .load(imageUrl)
            .into(image1)
    }

    private fun loadBooks(){
        var cursor: Cursor
        cursor = DB.rawQuery("SELECT * FROM DB이름", null)

        var num: Int = 0
        while(cursor.moveToNext()) {
//            var str_cover = cursor.getString(cursor.getColumnIndex("cover_image")).toString()
//            var str_title = cursor.getString(cursor.getColumnIndex("title")).toString()
//
//            addBook(str_cover, str_title, num)
//            num++
        }
        cursor.close()
    }

    private fun addBook(cover: String, title: String, id: Int){
        var shelflayout = LinearLayout(context).apply{
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                setMargins(45, 30, 0, 0)
            }
            this.id = id
        }
        var bookcover = ImageView(context).apply{
            layoutParams = LinearLayout.LayoutParams(75, 95)
            scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(context)
                .load(cover)
                .into(this)
        }

        var booktitle = TextView(context).apply{
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
            }
            text = title
        }

        shelflayout.addView(bookcover)
        shelflayout.addView(booktitle)
        layout.addView(shelflayout)
    }
}