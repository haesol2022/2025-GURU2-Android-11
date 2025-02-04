package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MeetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_meeting)

        // 네비게이션바 속성
        val homeButton = findViewById<ImageButton>(R.id.home_button)
        val calendarButton = findViewById<ImageButton>(R.id.calendar_button)
        val editButton = findViewById<ImageButton>(R.id.edit_button)
        val userButton = findViewById<ImageButton>(R.id.user_button)

        // 홈 버튼 클릭 시
        homeButton.setOnClickListener {
            // 투두리스트 화면으로 이동
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // 일정 관리 버튼 클릭 시
        calendarButton.setOnClickListener {
            // 일정 관리 화면으로 이동
            val intent = Intent(this, MeetingActivity::class.java)
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

        // MeetingFragment를 추가
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MeetingFragment())
                .commit()
        }
    }
}
