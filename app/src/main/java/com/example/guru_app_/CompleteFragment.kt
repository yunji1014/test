package com.example.guru_app_

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CompleteFragment : Fragment() {
    private lateinit var bookImageAdapter: BookImageAdapter
    private lateinit var bookDatabaseHelper: BookDatabaseHelper
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(context, 3) // 두 번째 인자는 열의 수
        recyclerView.layoutManager = gridLayoutManager

        bookDatabaseHelper = BookDatabaseHelper(requireContext())
        val books = bookDatabaseHelper.getAllBooks().filter { it.status == "endreading" }

        bookImageAdapter = BookImageAdapter(requireContext(), books)
        recyclerView.adapter = bookImageAdapter
    }
}
