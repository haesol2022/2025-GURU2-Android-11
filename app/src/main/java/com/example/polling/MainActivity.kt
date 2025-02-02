package com.example.polling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.polling.MeetingListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 프래그먼트 초기화
        if (savedInstanceState == null) {
            loadFragment(MeetingListFragment())  // SummarizeFragment 대신 MeetingListFragment를 로드
        }
    }

    // 프래그먼트를 로드하는 메서드
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // fragment_container는 activity_main.xml에서 정의한 ID
            .commit()
    }
}
