package com.example.polling.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.polling.R

class MeetingAdapter(private val meetings: MutableList<String>, private val onDelete: (Int) -> Unit) :
    RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class MeetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val etMeetingTitle: EditText = view.findViewById(R.id.etMeetingTitle)
        val btnDeleteMeeting: ImageView = view.findViewById(R.id.btnDeleteMeeting)

        init {
            // 삭제 버튼 클릭
            btnDeleteMeeting.setOnClickListener {
                onDelete(adapterPosition)
            }

            // RecyclerView 항목 클릭
            itemView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meeting_item, parent, false)
        return MeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meetingTitle = if (position in meetings.indices) {
            meetings[position]
        } else {
            ""
        }

        holder.etMeetingTitle.setText(meetingTitle)

        // EditText에서 포커스가 벗어나면 리스트의 해당 항목에 값을 저장
        holder.etMeetingTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && position in meetings.indices) {
                val updatedValue = holder.etMeetingTitle.text.toString()
                meetings[position] = updatedValue
                Log.d("MeetingAdapter", "Position: $position, Updated Value: $updatedValue") // 로그 추가
            }
        }

        // addTextChangedListener에서 TextWatcher 객체 사용
        holder.etMeetingTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // TextWatcher의 beforeTextChanged 메서드, 현재는 구현하지 않음
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (position in meetings.indices) {
                    val updatedValue = charSequence.toString()
                    meetings[position] = updatedValue
                    Log.d("MeetingAdapter", "Position: $position, Updated Value: $updatedValue") // 로그 추가
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // TextWatcher의 afterTextChanged 메서드, 현재는 구현하지 않음
            }
        })
    }

    // 항목 삭제 시 meetings 리스트에서 데이터를 반영, 변경 사항을 RecyclerView에 알림
    fun removeItem(position: Int) {
        if (position in meetings.indices) {
            // 리스트에서 해당 항목 삭제
            val removedItem = meetings[position] // 삭제할 항목 값 확인
            meetings.removeAt(position)
            Log.d("MeetingAdapter", "Removed Position: $position, Removed Item: $removedItem") // 로그 추가

            // RecyclerView 갱신
            notifyItemRemoved(position)
            if (position < meetings.size) {
                // 리스트 범위 내에서만 업데이트를 갱신
                notifyItemRangeChanged(position, meetings.size - position)
            }
        }
    }

    override fun getItemCount(): Int = meetings.size
}