package com.example.guru_app_

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru_app_.database.BookDao

class ReadingFragment : Fragment() {
    private lateinit var bookImageAdapter: BookImageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookDao = BookDao(requireContext())

        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(context, 3) // 열의 수
        recyclerView.layoutManager = gridLayoutManager

        val books = bookDao.getAllBooks().filter { it.status == "reading" }

        bookImageAdapter = BookImageAdapter(requireContext(), books)
        recyclerView.adapter = bookImageAdapter
    }
}
