package com.example.guru2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.DayViewDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CalendarDecorator() {
//    // 모든 날짜에 대해 데코레이터를 적용
//    override fun shouldDecorate(day: CalendarDay): Boolean {
//        return true
//    }
//
//    override fun decorate(view: DayViewFacade) {
//        // 가로선을 추가
//        view.addSpan(object : android.text.style.LineBackgroundSpan {
//            override fun drawBackground(
//                canvas: Canvas?,
//                paint: Paint?,
//                start: Int,
//                end: Int,
//                lineHeight: Int,
//                text: CharSequence?,
//                textStart: Int,
//                textEnd: Int,
//                lineNumber: Int,
//                layout: android.text.Layout?
//            ) {
//                // 가로선을 그립니다. 여기서 선 색상 및 두께를 조정할 수 있습니다.
//                paint?.color = Color.RED // 선 색상 설정
//                paint?.strokeWidth = 5f // 선 두께 설정
//
//                // 날짜 아래쪽에 가로선을 그립니다.
//                canvas?.drawLine(
//                    0f,
//                    (lineHeight + 2).toFloat(),  // 선의 Y 위치 조정
//                    layout?.width?.toFloat() ?: 0f,
//                    (lineHeight + 2).toFloat(),  // 선의 Y 위치
//                    paint!!
//                )
//            }
//        })
//    }

}