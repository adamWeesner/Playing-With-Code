package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import weesner.adam.playingwithcode.utils.centerX
import weesner.adam.playingwithcode.utils.centerY
import weesner.adam.playingwithcode.utils.visualization.Point
import java.util.*

class SunflowerMathCanvas(context: Context) : View(context) {
    var paint = Paint()
    var circles = arrayListOf<Point>()
    var scale = 4f
    val random = Random(System.currentTimeMillis())


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.BLACK

        circles.forEach { circle ->
            circle.display(scale * 1.5f, canvas, paint)
            paint.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        next()
        return super.onTouchEvent(event)
    }

    fun next() {
        for (i in 0 until 1000)
            if (circles.size == 0) {
                circles.add(Point(centerX, centerY))
            } else {
                val count = circles.size
                val theta = count * Math.toRadians(137.5)
                val r = scale * Math.sqrt(count.toDouble())
                val x: Float = ((Math.cos(theta) * r) * scale).toFloat()
                val y: Float = ((Math.sin(theta) * r) * scale).toFloat()

                val point = Point(x + centerX, y + centerY)
                circles.add(point)
            }
        invalidate()
    }
}
