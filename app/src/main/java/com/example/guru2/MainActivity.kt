package com.example.guru2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCalendar = findViewById<ImageButton>(R.id.btn_calendar)
        val btnMeeting = findViewById<ImageButton>(R.id.btn_meeting)

        // 처음 실행 시 캘린더 프래그먼트 표시
        if (savedInstanceState == null) {
            replaceFragment(CalendarFragment())
        }

        // 캘린더 버튼 클릭 시
        btnCalendar.setOnClickListener {
            replaceFragment(CalendarFragment())
        }

        // 미팅 버튼 클릭 시
        btnMeeting.setOnClickListener {
            replaceFragment(MeetingFragment())
        }


    }

    // 프래그먼트 변경 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}