package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import weesner.adam.playingwithcode.utils.visualization.Point

class FibonacciSequenceCanvas(context: Context) : View(context) {
    var paint = Paint()
    var circles = arrayListOf<FibPoint>()
    var move = ""
    var scale = 4f //default .5f
    var rotate = 90

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK

        circles.forEach { circle ->
            val diameter = circle.fibNumber.toFloat() * scale
            canvas!!.drawRect(circle.xs - diameter, circle.ys - diameter, circle.xs + diameter, circle.ys + diameter, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        next()
        return super.onTouchEvent(event)
    }

    fun next() {
        if (circles.size == 0) {
            circles.add(FibPoint(width * .5f, height * .5f, 0))
        } else {
            val nextNumber = nextFib()
            val previous = circles[circles.size - 1]
            val newFib = FibPoint(previous.xs, previous.ys, nextNumber)
            val older = if (circles.size == 1) 0 else circles[circles.size - 2].fibNumber

            val distance = (previous.fibNumber.toFloat() + nextNumber.toFloat()) * scale
            val partialDist = (((previous.fibNumber.toFloat() * .5f) + older) - (nextNumber.toFloat() * .5f)) * scale
            println("partial of $partialDist for -2:$older -1:${previous.fibNumber} 0:$nextNumber")
            val distPoint: Point
            when (move) {
                "left" -> {
                    move = "down"
                    distPoint = Point(-distance, partialDist)
                }
                "down" -> {
                    move = "right"
                    distPoint = Point(partialDist, distance)
                }
                "right" -> {
                    move = "up"
                    distPoint = Point(distance, -partialDist)
                }
                "up" -> {
                    move = "left"
                    distPoint = Point(-partialDist, -distance)
                }
                else -> {
                    move = "down"
                    distPoint = Point(-distance, 0f)
                }
            }
            //rotate(newFib, -radians(rotate))
            val dist = newFib.add(distPoint)
            newFib.xs = dist.x
            newFib.ys = dist.y
            circles.add(newFib)
        }
        invalidate()
    }

    private fun nextFib(): Int = when (circles.size) {
        1 -> 1
        else -> circles[circles.size - 1].fibNumber + circles[circles.size - 2].fibNumber
    }

    class FibPoint(var xs: Float, var ys: Float, val fibNumber: Int) : Point(xs, ys)
}
