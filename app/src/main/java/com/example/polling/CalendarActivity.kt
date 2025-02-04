package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_calendar) // 액티비티의 레이아웃

        val homeButton = findViewById<ImageButton>(R.id.home_button)
        val calendarButton = findViewById<ImageButton>(R.id.calendar_button)
        val editButton = findViewById<ImageButton>(R.id.edit_button)
        val userButton = findViewById<ImageButton>(R.id.user_button)

        // 기존에 만들어 놓은 CalendarFragment를 액티비티에 추가
        if (savedInstanceState == null) {
            val fragment = CalendarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // 레이아웃에 프래그먼트 추가
                .commit()
        }

        // 홈 버튼 클릭 시
        homeButton.setOnClickListener {
            // 투두리스트 화면으로 이동
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // 일정 관리 버튼 클릭 시
//        calendarButton.setOnClickListener {
//            // 일정 관리 화면으로 이동
//            val intent = Intent(this, MeetingActivity::class.java)
//            startActivity(intent)
//        }

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
}