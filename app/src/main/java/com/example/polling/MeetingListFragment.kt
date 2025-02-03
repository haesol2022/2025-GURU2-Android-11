package com.example.polling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polling.R  // 패키지 경로 확인!
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

        meetingAdapter = MeetingAdapter(meetingList) { position ->
            removeMeeting(position)
        }
        recyclerView.adapter = meetingAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btnAddMeeting.setOnClickListener {
            addNewMeeting()
        }

        // RecyclerView 항목 클릭 리스너 추가
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

    private fun addNewMeeting() {
        meetingList.add("")
        meetingAdapter.notifyItemInserted(meetingList.size - 1)
    }

    private fun removeMeeting(position: Int) {
        meetingList.removeAt(position)
        meetingAdapter.notifyItemRemoved(position)
    }
}
