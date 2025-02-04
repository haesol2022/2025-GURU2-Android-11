package com.example.polling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polling.R
import com.example.polling.adapter.MeetingAdapter  // Adapter 가져오기

class MeetingListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var meetingAdapter: MeetingAdapter
    private val meetingList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meeting_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMeetings)
        val btnAddMeeting = view.findViewById<Button>(R.id.btnAddMeeting)

        // 어댑터 초기화, 각 항목 클릭 시 삭제 기능 추가
        meetingAdapter = MeetingAdapter(meetingList) { position ->
            removeMeeting(position)
        }

        recyclerView.adapter = meetingAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btnAddMeeting.setOnClickListener {
            addNewMeeting()
        }

        // RecyclerView 항목 클릭 리스너 추가 (세부 페이지로 이동)
        meetingAdapter.setOnItemClickListener { position ->
            // 세부 페이지로 이동
            val meetingDetailFragment = SummarizeFragment() // SummarizeFragment로 수정
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, meetingDetailFragment) // fragment_container로 수정
            transaction.addToBackStack(null) // 뒤로가기 스택에 추가
            transaction.commit()
        }

        return view
    }

    // 새로운 미팅 항목 추가
    private fun addNewMeeting() {
        meetingList.add("") // 빈 문자열 추가, 기본값으로 빈 값 사용
        meetingAdapter.notifyItemInserted(meetingList.size - 1) // 새로운 항목을 RecyclerView에 반영
    }

    // 미팅 항목 삭제 시, 리스트에 값 반영 후 삭제
    private fun removeMeeting(position: Int) {
        if (position in meetingList.indices) {
            // 항목을 삭제하기 전에 값 반영
            meetingList[position] = meetingList[position]  // 현재 EditText 값 반영

            // 리스트에서 해당 항목 삭제
            meetingList.removeAt(position)

            // RecyclerView에 업데이트 사항 반영
            meetingAdapter.notifyItemRemoved(position)

            // 삭제 후 나머지 항목에 대해서 갱신
            if (position < meetingList.size) {
                meetingAdapter.notifyItemRangeChanged(position, meetingList.size - position)
            }

        }
    }
}
