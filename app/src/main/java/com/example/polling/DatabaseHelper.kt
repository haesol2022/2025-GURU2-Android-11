package com.example.polling

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "memberDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE memberTBL (
                id TEXT PRIMARY KEY,
                password TEXT,
                nickname TEXT,
                email TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS memberTBL")
        onCreate(db)
    }

    // 아이디 중복 확인
    fun isIdExists(id: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM memberTBL WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // 회원가입
    fun registerUser(id: String, password: String, nickname: String, email: String): Boolean {
        if (isIdExists(id)) return false

        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", id)
            put("password", password)
            put("nickname", nickname)
            put("email", email)
        }

        val result = db.insert("memberTBL", null, values)
        db.close()
        return result != -1L
    }

    // 로그인 검증
    fun loginUser(id: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM memberTBL WHERE id = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(id, password))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }
}
