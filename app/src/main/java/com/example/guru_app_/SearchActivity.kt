package com.example.guru_app_

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.guru_app_.database.BookDatabaseHelper
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SearchActivity : AppCompatActivity() {

    lateinit var backButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var search: SearchView
    private val books = mutableListOf<Book>()
    private lateinit var dbHelper: BookDatabaseHelper // BookDatabaseHelper 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // BookDatabaseHelper 초기화
        dbHelper = BookDatabaseHelper(this)

        backButton = findViewById(R.id.BackButton)
        search = findViewById(R.id.search)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // BookAdapter 초기화 시 Context, books, dbHelper 전달
        bookAdapter = BookAdapter(this, books, dbHelper)
        recyclerView.adapter = bookAdapter

        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // SearchView에서 검색 버튼 클릭 이벤트 처리
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotBlank()) {
                    fetchBooks(query)
                } else {
                    Toast.makeText(this@SearchActivity, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 필요 시 실시간 검색을 위해 사용할 수 있습니다.
                return false
            }
        })

        // 인텐트에서 검색어를 받아와서 처리
        val query = intent.getStringExtra("query")
        if (query != null) {
            search.setQuery(query, true)
        } else {
            Toast.makeText(this, "검색어가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBooks(query: String) {
        val thread = Thread {
            try {
                val result = getNaverBookSearch(query)
                runOnUiThread {
                    parseBookInfo(result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "검색 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        thread.start()
    }

    private fun getNaverBookSearch(keyword: String): String {
        // 네이버 책 검색 API를 호출하는 코드
        val clientID = "qIL4TXtqeLNoiACa14G5"
        val clientSecret = "hPPiIzBqS3"
        val sb = StringBuilder()

        try {
            val text = URLEncoder.encode(keyword, "UTF-8")
            val apiURL = "https://openapi.naver.com/v1/search/book.json?query=$text"
            val url = URL(apiURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("X-Naver-Client-Id", clientID)
            connection.setRequestProperty("X-Naver-Client-Secret", clientSecret)
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                reader.close()
            } else {
                throw Exception("응답 코드: $responseCode")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sb.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseBookInfo(json: String) {
        try {
            val jsonObject = JSONObject(json)
            val items = jsonObject.optJSONArray("items")

            if (items != null) {
                books.clear() // 기존 데이터 초기화
                for (i in 0 until items.length()) {
                    val item = items.optJSONObject(i)
                    if (item != null) {
                        //val id = item.optInt("id")
                        val title = item.optString("title", "제목 없음")
                        val author = item.optString("author", "저자 없음")
                        val image = item.optString("image", "")
                        val isbn = item.optString("isbn", "ISBN 없음")
                        val publisher = item.optString("publisher", "출판사 없음")

                        //음 데이터를 추가하며 이가 제대로 된 데이터 값이 받아와지지
                        //않는 현상 발생. 그러나 오류가 없으려면 데이터 값을 받아와야 함..
                        //먼저 not null값을 가지는 데이터부터 확인 후 초기 status부터 해결
                        //지금은 status값 "상태없음" 으로 뜸.. 인식안됨 
                        books.add(Book(
                            title,
                            author,
                            image,
                            isbn,
                            publisher
                        ))
                    }
                }
                bookAdapter.notifyDataSetChanged()
            } else {
                Log.e("SearchActivity", "Items array is null")
                Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(this, "JSON 파싱 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
