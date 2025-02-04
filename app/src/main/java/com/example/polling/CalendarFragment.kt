package com.example.polling

import com.example.polling.DotDecorator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class CalendarFragment : Fragment() {
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var todoListLayout: LinearLayout
    private lateinit var addTodoButton: Button
    private lateinit var projectSelectLayout: LinearLayout
    private lateinit var projectDropdownLayout: LinearLayout
    private lateinit var btnAddProject: Button
    private var selectedDate: String = "" // 선택된 날짜
    private val addedDotDecorators = mutableMapOf<CalendarDay, DotDecorator>() // 추가된 도트를 추적

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // UI 컴포넌트 초기화
        calendarView = view.findViewById(R.id.calendarView)
        todoListLayout = view.findViewById(R.id.todo_list_layout)
        addTodoButton = view.findViewById(R.id.add_todo_button)

        projectSelectLayout = view.findViewById(R.id.project_select_layout)
        projectDropdownLayout = view.findViewById(R.id.project_dropdown_layout)
        btnAddProject = view.findViewById(R.id.btn_addProject)

        // 프로젝트 선택 레이아웃 클릭 시 드롭다운 토글
        projectSelectLayout.setOnClickListener {
            toggleProjectDropdown()
        }

        // 프로젝트 추가하기 버튼 클릭 시
        btnAddProject.setOnClickListener {
            showProjectNameDialog()
        }

        // 날짜 선택 시 selectedDate 업데이트
        calendarView.setOnDateChangedListener { _, date, _ ->
            val year = date.year
            val month = date.month
            val day = date.day
            selectedDate = "$year-$month-$day" // 선택된 날짜를 string 형식으로 저장

            // 선택된 날짜에 맞지 않는 항목들을 숨기기
            updateTodoItemsVisibility()
        }

        // "추가" 버튼 클릭 시 투두리스트 항목 추가
        addTodoButton.setOnClickListener {
            if (selectedDate.isNotEmpty()) {
                addTodoItem(selectedDate) // 선택된 날짜를 전달하여 Todo 추가

                // 선택된 날짜에 도트 표시 추가
                val selectedCalendarDay = CalendarDay.from(
                    selectedDate.split("-")[0].toInt(),
                    selectedDate.split("-")[1].toInt(),
                    selectedDate.split("-")[2].toInt()
                )
                if (!addedDotDecorators.containsKey(selectedCalendarDay)) {
                    val dotDecorator = DotDecorator(selectedCalendarDay)
                    calendarView.addDecorator(dotDecorator)
                    addedDotDecorators[selectedCalendarDay] = dotDecorator // 도트 저장
                }
            } else {
                // 날짜를 선택하지 않았을 경우, Toast 메시지 표시
                Toast.makeText(context, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // 투두리스트 항목을 추가하는 함수
    @SuppressLint("MissingInflatedId")
    private fun addTodoItem(date: String) {
        val context = requireContext()

        // todo_list.xml을 인플레이트하여 가져오기
        val todoLayout = LayoutInflater.from(context).inflate(R.layout.todo_list, null, false)

        val dayText: TextView = todoLayout.findViewById(R.id.dayText)
        val checkBox: CheckBox = todoLayout.findViewById(R.id.checkBox)
        val todoEditText: EditText = todoLayout.findViewById(R.id.todoEditText)
        val percentButton: Button = todoLayout.findViewById(R.id.percentButton)
        val deleteButton: ImageButton = todoLayout.findViewById(R.id.deleteButton)

        // 추가된 항목에 선택된 날짜 설정
        dayText.text = date

        // 체크박스 색상 설정
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                Color.parseColor("#4579FF"),  // 체크 상태의 색상
                Color.parseColor("#AAAAAA")   // 비체크 상태의 색상
            )
        )

        checkBox.buttonTintList = colorStateList

        // 체크박스 클릭 시 0.5초 후 삭제
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Handler(Looper.getMainLooper()).postDelayed({
                    todoListLayout.removeView(todoLayout)

                    // 해당 날짜의 도트도 삭제
                    checkAndRemoveDot(date)
                }, 500) // 500ms = 0.5초
            }
        }

        // 퍼센트 버튼 클릭 시 다이얼로그 표시
        percentButton.setOnClickListener {
            showSeekBarDialog(percentButton)
        }

        // 삭제 버튼 클릭 시 해당 항목 삭제
        deleteButton.setOnClickListener {
            todoListLayout.removeView(todoLayout)

            // 해당 날짜의 도트도 삭제
            checkAndRemoveDot(date)
        }

        // todo 항목을 전체 리스트 레이아웃에 추가
        todoListLayout.addView(todoLayout)
    }

    // 선택된 날짜에 맞지 않는 투두리스트 항목을 숨기는 함수
    private fun updateTodoItemsVisibility() {
        // 모든 투두리스트 항목을 확인하여 선택된 날짜와 비교
        for (i in 0 until todoListLayout.childCount) {
            val todoItem = todoListLayout.getChildAt(i)
            val dayText: TextView = todoItem.findViewById(R.id.dayText)

            // 만약 dayText와 선택된 날짜가 다르면 해당 항목을 GONE 처리
            if (dayText.text.toString() != selectedDate) {
                todoItem.visibility = View.GONE
            } else {
                todoItem.visibility = View.VISIBLE
            }
        }

        // 선택된 날짜에 도트가 있어야 하는지 확인하고, 없으면 도트 삭제
        checkAndRemoveDot(selectedDate)
    }

    // 도트를 추가하거나 삭제하는 함수
    private fun checkAndRemoveDot(date: String) {
        val dateParts = date.split("-")
        val calendarDay = CalendarDay.from(
            dateParts[0].toInt(),
            dateParts[1].toInt(),
            dateParts[2].toInt()
        )

        // 선택된 날짜에 투두리스트 항목이 없으면 도트를 제거
        var hasTodoItems = false
        for (i in 0 until todoListLayout.childCount) {
            val todoItem = todoListLayout.getChildAt(i)
            val dayText: TextView = todoItem.findViewById(R.id.dayText)
            if (dayText.text.toString() == date) {
                hasTodoItems = true
                break
            }
        }

        if (hasTodoItems) {
            // 항목이 있으면 도트를 추가
            if (!addedDotDecorators.containsKey(calendarDay)) {
                val dotDecorator = DotDecorator(calendarDay)
                calendarView.addDecorator(dotDecorator)
                addedDotDecorators[calendarDay] = dotDecorator
            }
        } else {
            // 항목이 없으면 도트를 제거
            addedDotDecorators[calendarDay]?.let {
                calendarView.removeDecorator(it)
                addedDotDecorators.remove(calendarDay)
            }
        }
    }

    // 퍼센트 수정 다이얼로그 표시
    private fun showSeekBarDialog(percentButton: Button) {
        context?.let { context ->
            val builder = AlertDialog.Builder(context)
            builder.setTitle("진척도 설정")

            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_seekbar, null)
            val seekBar: SeekBar = dialogView.findViewById(R.id.seekBar)
            val percentDisplay: TextView = dialogView.findViewById(R.id.percentDisplay)

            val currentPercent = percentButton.text.toString().replace("%", "").toIntOrNull() ?: 0
            seekBar.progress = currentPercent
            percentDisplay.text = "$currentPercent%"

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    percentDisplay.text = "$progress%"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            builder.setView(dialogView)

            builder.setPositiveButton("확인") { _, _ ->
                percentButton.text = "${seekBar.progress}%"
            }

            builder.setNegativeButton("취소", null)

            val dialog = builder.create()
            dialog.show()
        }
    }

    // 드롭다운 메뉴 보이기/숨기기
    private fun toggleProjectDropdown() {
        if (projectDropdownLayout.visibility == View.VISIBLE) {
            projectDropdownLayout.visibility = View.GONE
        } else {
            projectDropdownLayout.visibility = View.VISIBLE
        }
    }

    // 프로젝트 이름 입력 다이얼로그 표시
    private fun showProjectNameDialog() {
        val context = requireContext()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("프로젝트 이름 입력")

        // 사용자로부터 프로젝트 이름을 입력받을 EditText
        val input = EditText(context)
        input.setHint("프로젝트 이름")
        builder.setView(input)

        builder.setPositiveButton("확인") { _, _ ->
            val projectName = input.text.toString().trim()

            // 프로젝트 이름이 비어있지 않다면 드롭다운에 TextView 추가
            if (projectName.isNotEmpty()) {
                addProjectToDropdown(projectName)
            }
        }

        builder.setNegativeButton("취소", null)

        builder.create().show()
    }

    // 드롭다운에 프로젝트 이름 추가
    private fun addProjectToDropdown(projectName: String) {
        // project_list.xml을 인플레이트하여 가져오기
        val projectView = LayoutInflater.from(requireContext()).inflate(R.layout.project_list, null, false)
        //선택한 프로젝트의 이름이 나오는 곳
        val projectNameTextViewInSelectLayout = projectSelectLayout.findViewById<TextView>(R.id.project_name)

        // project_list.xml 내의 TextView를 찾아서 설정
        val projectNameTextView: TextView = projectView.findViewById(R.id.project_name_set)
        projectNameTextView.text = projectName

        // 삭제 버튼 클릭 리스너 추가
        val deleteButton: ImageButton = projectView.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // 해당 프로젝트 항목을 드롭다운에서 제거
            projectDropdownLayout.removeView(projectView)

            //현재 프로젝트를 삭제 할 경우 비우기
            projectNameTextViewInSelectLayout.text = ""
        }

        // 프로젝트 항목 클릭 시 해당 프로젝트 체크 표시
        projectView.setOnClickListener {
            // 모든 프로젝트 항목의 checkImg를 비활성화
            for (i in 0 until projectDropdownLayout.childCount) {
                val childView = projectDropdownLayout.getChildAt(i)
                val checkImg: ImageView? = childView.findViewById(R.id.checkImg)
                // checkImg가 null이 아닌지 확인하고 비활성화
                checkImg?.visibility = View.INVISIBLE // 비활성화
            }

            // 선택된 프로젝트 항목의 checkImg 활성화
            val checkImg: ImageView = projectView.findViewById(R.id.checkImg)
            checkImg.visibility = View.VISIBLE // 활성화

            // 선택된 프로젝트 이름을 표시할 TextView에 텍스트 설정
            projectNameTextViewInSelectLayout.text = projectName

            // 드롭다운 메뉴 닫기
            projectDropdownLayout.visibility = View.GONE
        }

        // Layout Params 설정
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 25 // 버튼과의 간격을 위해 margin 설정
        }

        projectView.layoutParams = params // 레이아웃 파라미터 적용

        // 드롭다운 레이아웃에 버튼 위에 추가하기
        projectDropdownLayout.addView(projectView, 0) // 0번째 위치에 추가
    }

}