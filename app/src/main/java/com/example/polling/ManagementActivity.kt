package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color

class ManagementActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var inputId: EditText
    private lateinit var inputRole: EditText
    private lateinit var buttonInput: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var memberLayout: LinearLayout

    private val addedMembers = mutableListOf<String>() // 추가된 멤버 아이디 저장 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_management)

        dbHelper = DatabaseHelper(this)

        // 전달받은 프로젝트 이름 가져오기
        val projectName = intent.getStringExtra("PROJECT_NAME") ?: ""

        // 프로젝트 이름을 화면에 표시할 TextView가 있다면 설정
        val projectTitleTextView = findViewById<TextView>(R.id.project_name)
        projectTitleTextView.text = projectName

        inputId = findViewById(R.id.input_id)
        inputRole = findViewById(R.id.input_role)
        buttonInput = findViewById(R.id.button_input)
        memberLayout = findViewById(R.id.team_list)
        buttonEdit = findViewById<Button>(R.id.button_edit)
        buttonDelete = findViewById<Button>(R.id.button_delete)

        // 네비게이션바 속성
        val homeButton = findViewById<ImageButton>(R.id.home_button)
        val calendarButton = findViewById<ImageButton>(R.id.calendar_button)
        val editButton = findViewById<ImageButton>(R.id.edit_button)
        val userButton = findViewById<ImageButton>(R.id.user_button)

        buttonInput.setOnClickListener {
            addMemberToList()
        }

        buttonEdit.setOnClickListener {
            editMemberRole()
        }

        buttonDelete.setOnClickListener {
            deleteMemberFromList()
        }

        // 홈 버튼 클릭 시
        homeButton.setOnClickListener {
            // 투두리스트 화면으로 이동
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // 일정 관리 버튼 클릭 시
        calendarButton.setOnClickListener {
            // 일정 관리 화면으로 이동
            val intent = Intent(this, MeetingActivity::class.java) // 미팅액티비티가 생기면 써주세요
            startActivity(intent)
        }

        // 회의록 버튼 클릭 시
        editButton.setOnClickListener {
            // 회의록 화면으로 이동
            val intent = Intent(this, MeetingListActivity::class.java)
            startActivity(intent)

        }

        // 유저 버튼 클릭 시
        userButton.setOnClickListener {
            // 멤버 관리 화면으로 이동
            val intent = Intent(this, ManagementActivity::class.java)
            startActivity(intent)
        }
    }

    // ID를 입력하면 닉네임 반환하는 함수
    private fun findNicknameById(id: String): String? {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT nickname
        FROM memberTBL
        WHERE id = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(id))

        var nickname: String? = null
        if (cursor.moveToFirst()) {
            nickname = cursor.getString(cursor.getColumnIndex("nickname"))
        }
        cursor.close()
        return nickname
    }

    // 멤버 추가 함수 (중복 방지 포함)
    private fun addMemberToList() {
        val id = inputId.text.toString().trim()
        val role = inputRole.text.toString().trim()

        if (id.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "아이디와 역할을 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 아이디가 유효한지 확인 (DB에 존재하는 아이디인지)
        if (!dbHelper.isIdExists(id)) {
            Toast.makeText(this, "유효하지 않은 아이디입니다", Toast.LENGTH_SHORT).show()
            return
        }

        // 중복 추가 방지
        if (addedMembers.contains(id)) {
            Toast.makeText(this, "이미 추가된 멤버입니다", Toast.LENGTH_SHORT).show()
            return
        }

        val nickname = findNicknameById(id)

        // 새로운 TextView 생성
        val newMemberTextView = TextView(this).apply {
            text = "$id ($nickname)\n$role" // 아이디와 역할 표시
            tag = role // 최신 역할 정보를 저장
            textSize = 16f
            setTextColor(context.getColor(R.color.gray))
            setPadding(8, 8, 8, 8)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8) // 여백 추가
            }

            // 클릭 이벤트 추가
            setOnClickListener {
                inputId.setText(id) // 클릭한 TextView의 ID를 EditText에 입력
                inputRole.setText(tag as String) // 클릭한 TextView의 최신 역할을 EditText에 입력
            }
        }

        // LinearLayout 내부에 추가
        memberLayout.addView(newMemberTextView)

        // 추가된 멤버 리스트에 아이디 저장
        addedMembers.add(id)

        // 입력 필드 초기화
        inputId.text.clear()
        inputRole.text.clear()
    }


    // 멤버 역할 수정 함수
    private fun editMemberRole() {
        val id = inputId.text.toString().trim()
        val newRole = inputRole.text.toString().trim()

        if (id.isEmpty() || newRole.isEmpty()) {
            Toast.makeText(this, "아이디와 새로운 역할을 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 아이디가 유효한지 확인 (DB에 존재하는 아이디인지)
        if (!dbHelper.isIdExists(id)) {
            Toast.makeText(this, "유효하지 않은 아이디입니다", Toast.LENGTH_SHORT).show()
            return
        }

        // 추가된 멤버 리스트에서 해당 아이디를 확인
        if (!addedMembers.contains(id)) {
            Toast.makeText(this, "수정할 멤버가 추가되어 있지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        // 멤버 정보를 수정
        for (i in 0 until memberLayout.childCount) {
            val child = memberLayout.getChildAt(i) as? TextView
            if (child != null && child.text.contains(id)) {
                val nickname = findNicknameById(id)
                child.text = "$id ($nickname)\n$newRole" // 역할 변경
                child.tag = newRole // 최신 역할 정보를 업데이트
                Toast.makeText(this, "멤버 정보가 수정되었습니다", Toast.LENGTH_SHORT).show()
                break
            }
        }

        // 입력 필드 초기화
        inputId.text.clear()
        inputRole.text.clear()
    }

    // 멤버 삭제 함수
    private fun deleteMemberFromList() {
        val id = inputId.text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "삭제할 멤버의 아이디를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 추가된 멤버 리스트에서 해당 아이디를 확인
        if (!addedMembers.contains(id)) {
            Toast.makeText(this, "삭제할 멤버가 추가되어 있지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        // 멤버 삭제
        for (i in 0 until memberLayout.childCount) {
            val child = memberLayout.getChildAt(i) as? TextView
            if (child != null && child.text.contains(id)) {
                // TextView를 LinearLayout에서 제거
                memberLayout.removeView(child)

                // 추가된 멤버 리스트에서 아이디 제거
                addedMembers.remove(id)

                Toast.makeText(this, "멤버가 삭제되었습니다", Toast.LENGTH_SHORT).show()
                break
            }
        }

        // 입력 필드 초기화
        inputId.text.clear()
        inputRole.text.clear()
    }
}