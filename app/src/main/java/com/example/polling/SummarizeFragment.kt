package com.example.polling

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.polling.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class SummarizeFragment : Fragment() {

    private lateinit var etNotes: EditText
    private lateinit var tvSummary: TextView
    private lateinit var btnSummarize: Button
    private lateinit var btnBack: ImageView


    private val OPENAI_API_KEY = BuildConfig.OPENAI_API_KEY  // OpenAI API 키 입력
    private val API_URL = "https://api.openai.com/v1/chat/completions"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summarize, container, false)


        etNotes = view.findViewById(R.id.etNotes)
        tvSummary = view.findViewById(R.id.tvSummary)
        btnSummarize = view.findViewById(R.id.btnSummarize)
        btnBack = view.findViewById(R.id.btnBack) as ImageView


        tvSummary.movementMethod = ScrollingMovementMethod.getInstance()

        // Meeting 데이터 받기
        val meetingTitle = arguments?.getString("meetingTitle")
        // 필요한 경우 추가 데이터 받기

        // 예시로 제목을 TextView에 표시
        tvSummary.text = meetingTitle


        btnSummarize.setOnClickListener {
            val notes = etNotes.text.toString()
            if (notes.isNotEmpty()) {
                summarizeNotes(notes)
            } else {
                tvSummary.visibility = View.VISIBLE
                tvSummary.text = "회의록 내용을 입력해주세요."
                tvSummary.setTextColor(Color.parseColor("#666666")) // 텍스트 색상을 회색으로 설정
            }
        }

        btnBack.setOnClickListener {
            val intent = Intent(activity, MeetingListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // 기존 MeetingListActivity가 있다면 재사용
            startActivity(intent)
            activity?.finish() // 현재 Activity 종료
        }

        return view
    }

    private fun summarizeNotes(text: String) {
        if (OPENAI_API_KEY.isEmpty()) {
            tvSummary.visibility = View.VISIBLE
            tvSummary.text = "API 키가 설정되지 않았습니다."
            return
        }

        Log.d("API_KEY", "API Key: $OPENAI_API_KEY")  // 로그로 API 키 값 확인

        tvSummary.visibility = View.VISIBLE
        tvSummary.text = "요약 중..."

        val client = OkHttpClient()

        val requestBody = JSONObject().apply {
            put("model", "gpt-4")
            put("messages", JSONArray().apply {
                put(JSONObject().put("role", "system").put("content", "너는 전문적인 문서 요약가야."))
                put(JSONObject().put("role", "user").put("content", text)) // 여기 수정
            })
            put("max_tokens", 150)
            put("temperature", 0.7)
        }

        val request = Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer $OPENAI_API_KEY")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    tvSummary.text = "요약 실패: 네트워크 오류"
                    Log.e("API_ERROR", "Network error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    activity?.runOnUiThread {
                        tvSummary.text = "요약 실패: 서버 오류 (${response.code})"
                    }
                    Log.e("API_ERROR", "Server error: ${response.code} ${response.message}")
                    return
                }

                val responseBody = response.body?.string()
                Log.d("API_RESPONSE", "Response body: $responseBody")

                if (responseBody.isNullOrEmpty()) {
                    activity?.runOnUiThread {
                        tvSummary.text = "요약 실패: 응답이 없습니다."
                    }
                    return
                }

                try {
                    // GsonBuilder로 Lenient 설정을 활성화
                    val gson = GsonBuilder()
                        .setLenient()  // Lenient 모드 활성화
                        .create()

                    // 응답을 JSON으로 파싱
                    val jsonResponse = gson.fromJson(responseBody, Map::class.java)

                    val choices = (jsonResponse["choices"] as? List<Map<String, Any>>)
                    val message = choices?.get(0)?.get("message") as? Map<String, Any>
                    val summaryText = message?.get("content") as? String ?: "요약할 수 없습니다."

                    activity?.runOnUiThread {
                        tvSummary.text = summaryText
                    }
                } catch (e: Exception) {
                    activity?.runOnUiThread {
                        tvSummary.text = "요약 실패: 응답 처리 오류"
                    }
                    Log.e("API_ERROR", "Response parsing error: ${e.message}")
                }
            }
        })
    }

}
