package com.example.polling

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.polling.MeetingListFragment

class MeetingListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_meeting_list)

        // 회의록 화면 프래그먼트 로드
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MeetingListFragment())  // 회의록 프래그먼트를 추가
            .commit()
    }
}
