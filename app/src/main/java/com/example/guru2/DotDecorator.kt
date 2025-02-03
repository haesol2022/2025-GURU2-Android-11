import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import android.graphics.Color
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class DotDecorator(private val date: CalendarDay) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        // 해당 날짜가 일치하면 도트를 추가
        return day == date
    }

    override fun decorate(view: DayViewFacade?) {
        // 도트를 그리기 위해 DotSpan 사용
        view?.addSpan(DotSpan(13F, Color.GRAY))
    }
}
