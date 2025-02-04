package com.example.polling

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "memberDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // 회원 테이블 생성
        val createMemberTable = """
            CREATE TABLE memberTBL (
                id TEXT PRIMARY KEY,
                password TEXT,
                nickname TEXT,
                email TEXT
            )
        """.trimIndent()
        db.execSQL(createMemberTable)

        // 프로젝트 테이블 생성
        val createProjectTable = """
            CREATE TABLE projectTBL (
                project_name TEXT,
                id TEXT,
                nickname TEXT,
                FOREIGN KEY (id) REFERENCES memberTBL(id),
                FOREIGN KEY (nickname) REFERENCES memberTBL(nickname),
                PRIMARY KEY (project_name, id)
            )
        """.trimIndent()
        db.execSQL(createProjectTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS memberTBL")
        db.execSQL("DROP TABLE IF EXISTS projectTBL")
        db.execSQL("DROP TABLE IF EXISTS todolistTBL")
        db.execSQL("DROP TABLE IF EXISTS scheduleTBL")
        db.execSQL("DROP TABLE IF EXISTS meetingTBL")
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

    // 닉네임 가져오기
    fun getUserNickname(id: String): String {
        val db = readableDatabase
        val query = "SELECT nickname FROM memberTBL WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id))

        var nickname = ""
        if (cursor.moveToFirst()) {
            nickname = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return nickname
    }

    // 프로젝트 추가 함수
    fun insertProject(projectName: String, id: String, nickname: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("project_name", projectName)
            put("id", id)
            put("nickname", nickname)
        }

        val result = db.insert("projectTBL", null, values)
        db.close()
        return result != -1L
    }

    // to do list 데이터 저장
    fun insertTodo(projectName: String, id: String, date: String, work: String, percent: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("project_name", projectName)
            put("id", id)
            put("date", date)
            put("work", work)
            put("percent", percent)
        }

        val result = db.insert("todolistTBL", null, values)
        db.close()
        return result != -1L
    }

    // 데이터 불러 오기
    fun getTodosByDate(id: String, date: String): List<Pair<String, Int>> {
        val db = readableDatabase
        val query = "SELECT work, percent FROM todolistTBL WHERE id = ? AND date = ?"
        val cursor = db.rawQuery(query, arrayOf(id, date))

        val todoList = mutableListOf<Pair<String, Int>>()
        while (cursor.moveToNext()) {
            val work = cursor.getString(0)
            val percent = cursor.getInt(1)
            todoList.add(Pair(work, percent))
        }
        cursor.close()
        db.close()
        return todoList
    }

}
