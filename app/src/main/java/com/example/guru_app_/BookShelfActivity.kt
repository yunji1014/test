package com.example.guru_app_.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru_app_.ComleteFragment
import com.example.guru_app_.R
import com.example.guru_app_.adapters.BookAdapter
import com.example.guru_app_.database.BookDao
import com.google.android.material.tabs.TabLayout
import com.example.guru_app_.ReadingFragment

class BookShelfActivity : AppCompatActivity() {
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_shelf)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = gridLayoutManager

        bookDao = BookDao(this)
        val books = bookDao.getAllBooks().toMutableList() // List<Book>를 MutableList<Book>로 변환

        recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(this, books, bookDao)
        recyclerView.adapter = bookAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val readingFragment: Fragment = ReadingFragment()
        val completedFragment: Fragment = ComleteFragment()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> supportFragmentManager.beginTransaction().replace(R.id.main_view, readingFragment).commit()
                    1 -> supportFragmentManager.beginTransaction().replace(R.id.main_view, completedFragment).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
