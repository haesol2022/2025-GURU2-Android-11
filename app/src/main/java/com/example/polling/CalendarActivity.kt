package com.example.polling

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_calendar) // 액티비티의 레이아웃

        // 기존에 만들어 놓은 CalendarFragment를 액티비티에 추가
        if (savedInstanceState == null) {
            val fragment = CalendarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // 레이아웃에 프래그먼트 추가
                .commit()
        }
    }
}
