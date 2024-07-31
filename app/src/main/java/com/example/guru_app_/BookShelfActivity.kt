package com.example.guru_app_

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru_app_.database.BookDatabaseHelper
import com.google.android.material.tabs.TabLayout

class BookShelfActivity : AppCompatActivity() {
    private lateinit var bookImageAdapter: BookImageAdapter
    private lateinit var bookDatabaseHelper: BookDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_shelf)

        // RecyclerView 설정
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // GridLayoutManager 설정
        val gridLayoutManager = GridLayoutManager(this, 3) // 두 번째 인자는 열의 수
        recyclerView.layoutManager = gridLayoutManager


        bookDatabaseHelper = BookDatabaseHelper(this)
        val books = bookDatabaseHelper.getAllBooks()

        //val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookImageAdapter = BookImageAdapter(this, books)
        recyclerView.adapter = bookImageAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val readingFragment: Fragment = ReadingFragment()
        val completeFragment: Fragment = ComleteFragment()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab){
                when(tab.position){
                    0 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, readingFragment).commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, completeFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}