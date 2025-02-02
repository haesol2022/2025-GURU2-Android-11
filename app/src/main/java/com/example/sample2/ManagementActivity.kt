package com.example.sample2

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class ManagementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_main)
        
        // 추후 업로드
    }
}