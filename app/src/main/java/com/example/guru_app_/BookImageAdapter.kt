package com.example.guru_app_

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookImageAdapter(private val context: Context, private val books: List<Book>) : RecyclerView.Adapter<BookImageAdapter.BookImageViewHolder>() {

    // ViewHolder 클래스 정의, 각 아이템의 뷰를 보유
    class BookImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImageButton: ImageButton = view.findViewById(R.id.bookImageButton)
    }

    // ViewHolder를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_image, parent, false)
        return BookImageViewHolder(view)
    }

    // ViewHolder에 데이터를 바인딩하는 메서드
    override fun onBindViewHolder(holder: BookImageViewHolder, position: Int) {
        val book = books[position] // 현재 위치의 책 객체를 가져옴

        // 책 이미지가 비어 있지 않으면 이미지를 로드
        if (book.image.isNotEmpty()) {
            Glide.with(holder.bookImageButton.context).load(book.image).into(holder.bookImageButton)
        } else {
            // 기본 이미지 설정 (예: placeholder_image)
            // holder.bookImageButton.setImageResource(R.drawable.placeholder_image)
        }

        // 이미지 버튼 클릭 시 동작 설정
        holder.bookImageButton.setOnClickListener {
            // 이미지 버튼 클릭 시 동작 설정
            val intent = Intent(context, HomeActivity::class.java) // HomeActivity로 전환하는 인텐트 생성
            context.startActivity(intent) // 액티비티 시작
        }
    }

    // 아이템의 총 개수를 반환하는 메서드
    override fun getItemCount(): Int {
        return books.size // 책 리스트의 크기를 반환
    }
}