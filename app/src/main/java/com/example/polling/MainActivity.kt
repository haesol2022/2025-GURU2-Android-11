package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.polling.MeetingListFragment
import  com.example.polling.SummarizeFragment

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

                    // 로그인 성공 시 MainActivity 종료
                    finish()  // MainActivity 종료
                    // 새로운 Activity로 이동 (회의록 페이지)
                    val intent = Intent(this, MeetingListActivity::class.java)
                    startActivity(intent)

                    finish()
                    // 로그인 성공 시 이동할 레이아웃 ("추후 입력 필요" 지우고 입력)
                    // val intent = Intent(this, "추후 입력 필요"::class.java)
                    // startActivity(intent)

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
    }

    // 프래그먼트를 로드하는 메서드
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)  // activity_main.xml에 있는 fragment_container에 프래그먼트 추가
            .commit()
    }
}
