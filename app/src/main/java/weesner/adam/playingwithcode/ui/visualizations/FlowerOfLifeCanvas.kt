package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import weesner.adam.playingwithcode.utils.radians
import weesner.adam.playingwithcode.utils.rotate
import weesner.adam.playingwithcode.utils.visualization.Point

class FlowerOfLifeCanvas(context: Context) : View(context) {
    var paint = Paint()
    var circles = arrayListOf<ArrayList<Point>>()
    val radius = 64f

    var counter = 1
    var oldCirclesCount = 0
    var newCirclesCount = 1
    var newCirclesArray = arrayListOf<Point>()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK

        circles.forEach {
            it.forEach { circle ->
                canvas!!.drawCircle(circle.x, circle.y, radius, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        calculateNext()
        return super.onTouchEvent(event)
    }

    fun calculateNext() {
        if (circles.isEmpty() || circles[circles.size - 1].size == newCirclesCount) {
            counter = if (circles.size == 0) 1 else circles.size + 1
            newCirclesCount = (Math.pow(counter.toDouble(), 3.0) - Math.pow((((counter - 1).toDouble())), 3.0)).toInt()
            newCirclesArray = arrayListOf()
            oldCirclesCount = (Math.pow((counter - 1).toDouble(), 3.0) - Math.pow(((((counter - 1) - 1).toDouble())), 3.0)).toInt()
        }

        if (newCirclesCount == 1) {
            newCirclesArray.add(Point(width * .5f, height * .5f))
        } else {
            addNewPoint()
            println("newCircles: $newCirclesArray")
        }

        circles.add(newCirclesArray)
        println("circles: ${circles[circles.size - 1].size}")
        invalidate()
    }

    private fun addNewPoint() {
        val startingPoint = circles[0][0]
        var newPoint: Point
        if (newCirclesArray.size == 0) {
            newPoint = Point(startingPoint.x, startingPoint.y - (radius * circles.size))
        } else {
            val oldPoint = newCirclesArray[newCirclesArray.size - 1]
            newPoint = Point(oldPoint.x, oldPoint.y)
            rotate(newPoint, -radians(60))
            newPoint = newPoint.subtract(startingPoint)
            println("newPoint: $newPoint")
        }
        newCirclesArray.add(newPoint)
    }
}