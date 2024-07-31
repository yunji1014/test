package com.example.guru_app_

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.example.guru_app_.database.BookDatabaseHelper


class BookAdapter(private val context: Context, private val books: List<Book>, private val dbHelper: BookDatabaseHelper) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // BookViewHolder 클래스는 각각의 아이템 뷰를 보유하며, 뷰의 각 구성 요소를 초기화
    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.imageView)
        val bookTitle: TextView = view.findViewById(R.id.textTitle)
        val bookAuthor: TextView = view.findViewById(R.id.textAuthor)
        val bookISBN: TextView = view.findViewById(R.id.bookISBN)
        val bookPublisher: TextView = view.findViewById(R.id.bookPublisher)
        val bookCategory: TextView = view.findViewById(R.id.bookCategory)
        val addBookButton: Button = view.findViewById(R.id.AddBookButton) // AddBookButton 추가
    }

    // onCreateViewHolder 메서드는 새로운 ViewHolder 객체를 생성할 때 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        // item_search_book 레이아웃을 인플레이트하여 새로운 뷰를 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_book, parent, false)
        return BookViewHolder(view)
    }

    // onBindViewHolder 메서드는 ViewHolder와 데이터를 바인딩할 때 호출
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position] // 현재 위치의 책 데이터를 가져옴
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = book.author
        holder.bookISBN.text = "ISBN: ${book.isbn}"
        holder.bookPublisher.text = "출판사: ${book.publisher}"
        holder.bookCategory.text = "카테고리: ${book.category}"

        // 책 이미지가 있을 경우 이미지를 설정
        if (book.image.isNotEmpty()) {
            Glide.with(holder.bookImage.context).load(book.image).into(holder.bookImage)
        } else {
            //기본이미지 추가 가능. 아래는 예시..
            //holder.bookImage.setImageResource(R.drawable.placeholder_image)
        }

        // AddBookButton 클릭 리스너 설정
        holder.addBookButton.setOnClickListener {
            dbHelper.addBook(book)  // 책 데이터를 데이터베이스에 저장

            // BookShelf 시작하여 책 목록을 표시
            val intent = Intent(context, BookShelf::class.java)
            context.startActivity(intent)
        }
    }

    // getItemCount 메서드는 아이템의 총 개수를 반환
    override fun getItemCount(): Int {
        return books.size
    }
}