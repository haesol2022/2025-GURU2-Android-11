package com.example.sample2

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
    }

    // 비밀번호 찾기 함수
    private fun findPassword(id: String, email: String): String? {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT $COLUMN_PASSWORD 
            FROM ${DatabaseHelper.TABLE_USERS} 
            WHERE ${DatabaseHelper.COLUMN_ID} = ? AND ${DatabaseHelper.COLUMN_EMAIL} = ?
        """
        val cursor = db.rawQuery(query, arrayOf(id, email))

        var password: String? = null
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD))
        }
        cursor.close()
        return password
    }

    companion object {
        const val COLUMN_PASSWORD = "password"
    }
}