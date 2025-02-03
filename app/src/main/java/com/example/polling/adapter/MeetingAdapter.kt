package com.example.polling.adapter

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
        holder.etMeetingTitle.setText(meetings[position])

        holder.etMeetingTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                meetings[position] = holder.etMeetingTitle.text.toString()
            }
        }
    }

    override fun getItemCount(): Int = meetings.size
}
