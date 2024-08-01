package com.example.guru_app_

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guru_app_.activities.BookMemoActivity
import com.example.guru_app_.database.BookDao
import com.example.guru_app_.models.Book

class BookImageAdapter(private val context: Context, private val books: List<Book>) : RecyclerView.Adapter<BookImageAdapter.BookImageViewHolder>() {

    class BookImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImageButton: ImageButton = view.findViewById(R.id.bookImageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_image, parent, false)
        return BookImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookImageViewHolder, position: Int) {
        val book = books[position]
        val bookDao = BookDao(context) // context 전달 필요

        if (book.coverImage != null) {
            Glide.with(holder.bookImageButton.context).load(book.coverImage).into(holder.bookImageButton)
        } else {
            holder.bookImageButton.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.bookImageButton.setOnClickListener {
            val intent = Intent(context, BookMemoActivity::class.java).apply {
                putExtra("BOOK_ID", book.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}
