package com.example.guru_app_

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru_app_.database.BookDao

class HomeActivity : AppCompatActivity() {
    private lateinit var bookImageAdapter: BookImageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // BookDao 초기화
        bookDao = BookDao(this)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView) // ID를 다르게 설정
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // BookImageAdapter 초기화 및 설정
        val books = bookDao.getAllBooks()
        bookImageAdapter = BookImageAdapter(this, books, bookDao)
        recyclerView.adapter = bookImageAdapter

        val search = findViewById<SearchView>(R.id.search)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val intent = Intent(this@HomeActivity, SearchActivity::class.java)
                    intent.putExtra("query", query)
                    startActivity(intent)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}
