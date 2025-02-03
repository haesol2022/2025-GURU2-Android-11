package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_main)

        // 데이터베이스 헬퍼 초기화
        dbHelper = DatabaseHelper(this)

        // 로그인 UI 요소들
        val etId = findViewById<EditText>(R.id.et_id)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        val tvFindPassword = findViewById<TextView>(R.id.tv_find_password)

        // 캘린더 UI 요소들
        val btnCalendar = findViewById<ImageButton>(R.id.btn_calendar)
        val btnMeeting = findViewById<ImageButton>(R.id.btn_meeting)

        // 로그인 버튼 클릭 이벤트
        btnLogin.setOnClickListener {
            val id = etId.text.toString()
            val password = etPassword.text.toString()

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val isValid = dbHelper.loginUser(id, password)
                if (isValid) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    finish()  // MainActivity 종료
                    // 로그인 후 캘린더 화면으로 이동
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 회원 가입 화면으로 이동
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 비밀번호 찾기 화면으로 이동
        tvFindPassword.setOnClickListener {
            val intent = Intent(this, FindpwActivity::class.java)
            startActivity(intent)
        }

        // 캘린더 버튼 클릭 시
        btnCalendar.setOnClickListener {
            // 캘린더 화면으로 이동
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // 미팅 버튼 클릭 시
        btnMeeting.setOnClickListener {
            replaceFragment(MeetingFragment())
        }

        // 처음 실행 시 캘린더 프래그먼트 표시
        if (savedInstanceState == null) {
            replaceFragment(CalendarFragment())
        }
    }

    // 프래그먼트 변경 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)  // activity_main.xml에 있는 fragment_container에 프래그먼트 추가
            .commit()
    }
}
