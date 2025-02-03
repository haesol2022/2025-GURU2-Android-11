package com.example.polling

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.polling.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var todoListLayout: LinearLayout
    private lateinit var addTodoButton: Button
    private var selectedDate: String = "" // 현재 선택된 날짜

    // 날짜별 투두리스트 저장
    private val todoMap = mutableMapOf<String, MutableList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById<MaterialCalendarView>(R.id.calendarView)
        todoListLayout = view.findViewById(R.id.todo_list_layout)
        addTodoButton = view.findViewById(R.id.add_todo_button)

        // 오늘 날짜를 기본 선택
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        selectedDate = sdf.format(Date())  // 기본 날짜 설정

        // "추가" 버튼 클릭 시 투두리스트 추가
        addTodoButton.setOnClickListener {
            addTodoItem(selectedDate)
        }

        return view
    }

    // 투두리스트 항목을 추가하는 함수
    @SuppressLint("MissingInflatedId")
    private fun addTodoItem(date: String, todo: String? = null) {
        val context = requireContext()

        // todo_list.xml을 인플레이트하여 가져오기
        val todoLayout = LayoutInflater.from(context)
            .inflate(R.layout.todo_list, todoListLayout, false)

        // 체크박스, EditText, Button, ImageButton 등 참조
        val checkBox: CheckBox = todoLayout.findViewById(R.id.checkBox)
        val todoEditText: EditText = todoLayout.findViewById(R.id.todoEditText)
        val percentButton: Button = todoLayout.findViewById(R.id.percentButton)
        val deleteButton: ImageButton = todoLayout.findViewById(R.id.deleteButton)

        // 오늘 날짜를 기본 선택
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        selectedDate = sdf.format(Date())  // 기본 날짜 설정

        // 날짜마다 가로선 추가하는 데코레이터 설정
        //calendarView.addDecorator(CalendarDecorator())

        // 체크박스 클릭 시 삭제
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 0.5초 후에 해당 레이아웃을 삭제
                Handler(Looper.getMainLooper()).postDelayed({
                    todoListLayout.removeView(todoLayout)
                }, 500) // 500ms = 0.5초
            }
        }

        // 퍼센트 버튼 클릭 시 다이얼로그 표시
        percentButton.setOnClickListener {
            showSeekBarDialog(percentButton)
        }

        // 삭제 버튼 클릭 시 해당 항목 삭제
        deleteButton.setOnClickListener {
            // 0.5초 후에 해당 레이아웃을 삭제
            Handler(Looper.getMainLooper()).postDelayed({
                todoListLayout.removeView(todoLayout)
            }, 500) // 500ms = 0.5초
        }

        // 생성된 항목을 todoListLayout에 추가
        todoListLayout.addView(todoLayout)
    }

    // 퍼센트 수정 다이얼로그 표시
    private fun showSeekBarDialog(percentButton: Button) {
        // context가 null일 수 있으므로 안전하게 사용
        context?.let { context ->
            val builder = AlertDialog.Builder(context)
            builder.setTitle("진척도 설정")

            // 다이얼로그 레이아웃
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_seekbar, null)
            val seekBar: SeekBar = dialogView.findViewById(R.id.seekBar)
            val percentDisplay: TextView = dialogView.findViewById(R.id.percentDisplay)

            // SeekBar의 초기값 설정
            val currentPercent = percentButton.text.toString().replace("%", "").toIntOrNull() ?: 0
            seekBar.progress = currentPercent
            percentDisplay.text = "$currentPercent%"  // 퍼센트 디스플레이 초기화

            // SeekBar 값 변경 시 퍼센트 텍스트 업데이트
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    percentDisplay.text = "$progress%"  // 실시간으로 퍼센트 표시 업데이트
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            builder.setView(dialogView)

            // 확인 버튼 클릭 시 퍼센트 버튼 텍스트 업데이트
            builder.setPositiveButton("확인") { _, _ ->
                percentButton.text = "${seekBar.progress}%" // percentButton의 텍스트를 업데이트
            }

            builder.setNegativeButton("취소", null)

            val dialog = builder.create()
            dialog.show()
        }
    }
}