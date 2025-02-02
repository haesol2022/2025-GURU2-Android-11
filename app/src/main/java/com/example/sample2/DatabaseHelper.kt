package com.example.sample2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_NICKNAME TEXT,
                $COLUMN_EMAIL TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // 아이디 중복 확인
    fun isIdExists(id: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_ID = ?"
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
            put(COLUMN_ID, id)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NICKNAME, nickname)
            put(COLUMN_EMAIL, email)
        }

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    // 로그인 검증
    fun loginUser(id: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_ID = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(id, password))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    companion object {
        private const val DATABASE_NAME = "memberDB" // DB명
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "memberTBL" // 테이블명
        const val COLUMN_ID = "id"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NICKNAME = "nickname"
        const val COLUMN_EMAIL = "email"
    }
}