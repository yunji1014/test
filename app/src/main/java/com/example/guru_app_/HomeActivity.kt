package com.example.guru_app_

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru_app_.database.BookDao
import android.widget.Toast
import com.example.guru_app_.popapi.BestsellerResponse
import com.example.guru_app_.popapi.PopBookAdapter
import com.example.guru_app_.popapi.RetrofitClient
import com.example.guru_app_.search.SearchActivity
import com.example.guru_app_.shelf.BookImageAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private lateinit var bookImageAdapter: BookImageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var popBook: RecyclerView
    private lateinit var bookDao: BookDao
    private lateinit var bestsellerAdapter: PopBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // BookDao 초기화
        bookDao = BookDao(this)

        // RecyclerView 초기화 및 설정
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // BookImageAdapter 초기화 및 설정
        val books = bookDao.getAllBooks()
        bookImageAdapter = BookImageAdapter(this, books, bookDao)
        recyclerView.adapter = bookImageAdapter

        // popBook RecyclerView 초기화 및 설정
        popBook = findViewById(R.id.PopBook)
        popBook.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Bestseller 목록을 가져와 설정
        fetchBestsellers()

        // SearchView 설정
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

    private fun fetchBestsellers() {
        val apiKey = "ttb1014jiye1521001"
        val queryType = "Bestseller"
        val maxResults = 5
        val start = 1
        val searchTarget = "Book"

        Toast.makeText(this, "Starting API call", Toast.LENGTH_SHORT).show()

        RetrofitClient.instance.getBestsellers(apiKey, queryType, maxResults, start, searchTarget)
            .enqueue(object : Callback<BestsellerResponse> {
                override fun onResponse(call: Call<BestsellerResponse>, response: Response<BestsellerResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@HomeActivity, "API call successful", Toast.LENGTH_SHORT).show()
                        val books = response.body()?.items ?: mutableListOf()
                        bestsellerAdapter = PopBookAdapter(books)
                        popBook.adapter = bestsellerAdapter
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@HomeActivity, "API call failed: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BestsellerResponse>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, "API call failed: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
