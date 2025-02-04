package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FindpwActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_findpw)

        // DatabaseHelper 초기화
        dbHelper = DatabaseHelper(this)

        val etId = findViewById<EditText>(R.id.et_findpw_id)
        val etEmail = findViewById<EditText>(R.id.et_findpw_email)
        val btnSubmit = findViewById<Button>(R.id.btn_findpw_submit)
        val tvResult = findViewById<TextView>(R.id.tv_findpw_result)
        val btnBack = findViewById<Button>(R.id.btn_back)

        btnSubmit.setOnClickListener {
            val id = etId.text.toString()
            val email = etEmail.text.toString()

            if (id.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "아이디와 이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                val password = findPassword(id, email)
                if (password != null) {
                    tvResult.text = "비밀번호: $password"
                } else {
                    tvResult.text = "일치하는 정보를 찾을 수 없습니다."
                }
            }
        }

        btnBack.setOnClickListener {
            // 로그인 성공 시 FindpwActivity 종료
            finish()  // FindpwActivity 종료

            // 새로운 Activity로 이동 (로그인 페이지)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // 비밀번호 찾기 함수
    private fun findPassword(id: String, email: String): String? {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT password
        FROM memberTBL
        WHERE id = ? AND email = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(id, email))

        var password: String? = null
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex("password"))
        }
        cursor.close()
        return password
    }
}