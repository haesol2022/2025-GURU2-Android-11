package com.example.polling

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val etId = findViewById<EditText>(R.id.et_register_id)
        val btnCheckId = findViewById<Button>(R.id.btn_check_id)
        val etPassword = findViewById<EditText>(R.id.et_register_password)
        val etPasswordConfirm = findViewById<EditText>(R.id.et_register_password_confirm)
        val etNickname = findViewById<EditText>(R.id.et_register_nickname)
        val etEmail = findViewById<EditText>(R.id.et_register_email)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val btnBack = findViewById<Button>(R.id.btn_back)

        // 아이디 중복 확인
        btnCheckId.setOnClickListener {
            val id = etId.text.toString()
            if (id.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.isIdExists(id)) {
                Toast.makeText(this, "아이디가 중복되었습니다. 다른 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                etId.text.clear()
            } else {
                Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 처리
        btnRegister.setOnClickListener {
            val id = etId.text.toString()
            val password = etPassword.text.toString()
            val passwordConfirm = etPasswordConfirm.text.toString()
            val nickname = etNickname.text.toString()
            val email = etEmail.text.toString()

            if (id.isEmpty() || password.isEmpty() || nickname.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(password)) {
                Toast.makeText(this, "비밀번호는 8자 이상이며, 영문, 숫자, 특수문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
            } else if (password != passwordConfirm) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.registerUser(id, password, nickname, email)
                if (success) {
                    Toast.makeText(this, "회원 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "회원 가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
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

    // 비밀번호 유효성 검사 (영문, 숫자, 특수문자 포함 & 최소 8자 이상)
    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return passwordRegex.matches(password)
    }
}