package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class TouchableCircleCanvas(context: Context) : View(context) {
    var paint = Paint()
    var can: Canvas? = null
    var cirX: Float = 0f
    var cirY: Float = 0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        can = canvas
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.GREEN
        if (cirX == 0f && cirY == 0f) {
            cirX = width * .5f
            cirY = height * .5f
        }
        canvas!!.drawCircle(cirX, cirY, 100f, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        cirX = event!!.x
        cirY = event.y
        invalidate()
        return super.onTouchEvent(event)
    }
}