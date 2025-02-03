package com.example.polling

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.guru2.R
import java.util.Calendar

class MeetingFragment : Fragment() {

    private lateinit var meetingLayout: LinearLayout
    private lateinit var addMeetingButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meeting, container, false)

        // 뷰들 찾기
        meetingLayout = view.findViewById(R.id.meeting_layout)
        addMeetingButton = view.findViewById(R.id.add_meeting_button)

        // 버튼 클릭 리스너
        addMeetingButton.setOnClickListener {
            // 새로운 회의 항목 추가
            addMeetingItem()
        }

        return view
    }

    // 회의 항목을 추가하는 함수
    private fun addMeetingItem() {
        val context = requireContext()

        // meeting_list.xml을 인플레이트하여 새로운 뷰 생성
        val meetingItemView = LayoutInflater.from(context).inflate(R.layout.meeting_list, meetingLayout, false)

        // 'meeting_layout' 클릭 시 'detailsLayout' 보이거나 숨기기
        val meetingItemLayout: LinearLayout = meetingItemView.findViewById(R.id.meeting_layout)
        val detailsLayout: LinearLayout = meetingItemView.findViewById(R.id.detailsLayout)
        val meetingTitleEditText: EditText = meetingItemView.findViewById(R.id.meeting_title)
        val meetingDayTextView: TextView = meetingItemView.findViewById(R.id.meeting_day)
        val meetingTiemTextView: TextView = meetingItemView.findViewById(R.id.meeting_time)
        val dayText: TextView = meetingItemView.findViewById(R.id.dayText)

        // '회의 제목'을 수정할 수 있도록 visibility에 따라 처리
        meetingItemLayout.setOnClickListener {
            // detailsLayout의 visibility가 GONE이라면 VISIBLE로 변경, VISIBLE이라면 GONE으로 변경
            if (detailsLayout.visibility == View.GONE) {
                detailsLayout.visibility = View.VISIBLE
                meetingTitleEditText.isFocusable = true // 제목 입력 가능
                meetingTitleEditText.isEnabled = true
            } else {
                detailsLayout.visibility = View.GONE
                meetingTitleEditText.isFocusable = false // 제목 입력 불가능
                meetingTitleEditText.isEnabled = false
            }
        }

        // 'meeting_day' TextView 클릭 시 날짜 선택 다이얼로그 띄우기
        meetingDayTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    // 선택한 날짜를 TextView에 표시 (월은 0부터 시작하므로 1을 더함)
                    meetingDayTextView.text = "$selectedYear-${selectedMonth + 1}-$selectedDay"

                    // 날짜 계산: 남은 일수 구하기
                    val currentDate = Calendar.getInstance() // 오늘 날짜
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay) // 선택한 날짜

                    // 두 날짜의 차이 계산 (밀리초 단위)
                    val diffInMillis = selectedDate.timeInMillis - currentDate.timeInMillis

                    // 밀리초를 일수로 변환
                    val diffInDays = (diffInMillis / (24 * 60 * 60 * 1000)).toInt()

                    // 남은 일수를 TextView에 표시 (D-XX)
                    if (diffInDays > 0) {
                        dayText.text = "D-$diffInDays"
                    } else if (diffInDays == 0) {
                        dayText.text = "D-Day"
                    } else {
                        dayText.text = "회의 종료"
                    }
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        // 'meeting_time' TextView 클릭 시 시간 선택 다이얼로그 띄우기
        meetingTiemTextView.setOnClickListener {
            showTimePickerDialog(meetingTiemTextView)  // TextView를 파라미터로 전달
        }

        // 삭제 버튼 클릭 시 해당 회의 항목 삭제
        val deleteButton: ImageButton = meetingItemView.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            meetingLayout.removeView(meetingItemView)
        }

        // '회의 항목'을 버튼 위에 추가
        val buttonIndex = meetingLayout.indexOfChild(addMeetingButton) // 버튼 위치 인덱스 찾기
        meetingLayout.addView(meetingItemView) // 버튼 위에 추가
    }

    // TimePicker를 다이얼로그 형태로 보여주는 함수
    private fun showTimePickerDialog(meetingTiemTextView: TextView) {
        val calendarInstance = Calendar.getInstance()
        val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val minute = calendarInstance.get(Calendar.MINUTE)

        // TimePickerDialog의 시간 선택 리스너
        val onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->
            if (isAdded) {  // Fragment가 현재 액티비티에 붙어 있을 때만 동작하도록 확인
                calendarInstance.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendarInstance.set(Calendar.MINUTE, minuteOfHour)
                val timeString = String.format("%02d:%02d", hourOfDay, minuteOfHour)
                meetingTiemTextView.text = timeString  // 여기서 해당 TextView에 시간을 표시
            }
        }

        // TimePickerDialog 생성
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            onTimeSetListener,
            hour,
            minute,
            true
        )

        timePickerDialog.setTitle("시간 선택")
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }
}