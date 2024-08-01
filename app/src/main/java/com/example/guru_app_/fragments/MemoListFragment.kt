package com.example.guru_app_.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru_app_.R
import com.example.guru_app_.adapters.MemoListAdapter
import com.example.guru_app_.adapters.RecyclerItemClickListener
import com.example.guru_app_.database.MemoDao
import com.example.guru_app_.models.Memo

class MemoListFragment : Fragment() {

    private lateinit var memoListAdapter: MemoListAdapter
    private lateinit var memoDao: MemoDao
    private var listener: MemoItemClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MemoItemClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MemoItemClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.memo_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        memoDao = MemoDao(requireContext())
        refreshMemoList()

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), recyclerView, object :
                RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val memo = memoListAdapter.getMemoAt(position)
                    listener?.onMemoItemClick(memo.id ?: -1)
                }

                override fun onLongItemClick(view: View, position: Int) {
                    // long click event handling
                }
            })
        )
    }

    fun refreshMemoList() {
        val memos = memoDao.getMemosForBook(1) // 임의의 bookId 1 사용
        Log.d("MemoListFragment", "Loaded memos: $memos") // 로드된 메모를 로그로 출력
        memoListAdapter = MemoListAdapter(memos)
        view?.findViewById<RecyclerView>(R.id.memo_recycler_view)?.adapter = memoListAdapter
    }

    interface MemoItemClickListener {
        fun onMemoItemClick(memoId: Int)
    }
}
