package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import weesner.adam.playingwithcode.model.visualization.KochLine
import weesner.adam.playingwithcode.utils.deviceHeight
import weesner.adam.playingwithcode.utils.deviceWidth
import weesner.adam.playingwithcode.utils.visualization.Point

class KochLineCanvas(context: Context) : View(context) {
    var touches = 0
    var paint = Paint()
    var lines = arrayListOf<KochLine>()

    init {
        restart()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.BLACK

        for (line: KochLine in lines) line.display(canvas, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (touches <= 5) {
            touches++
            generate()
        } else {
            touches = 0
            restart()
        }
        return super.onTouchEvent(event)
    }

    private fun restart() {
        lines.clear()
        val a = Point(deviceWidth * .3f, 0f)
        val b = Point(deviceWidth * .3f, deviceHeight.toFloat())

        lines.add(KochLine(a, b))
        invalidate()
    }

    private fun generate() {
        val next = arrayListOf<KochLine>()
        for (line: KochLine in lines) {
            val a = line.kochA()
            val b = line.kochB()
            val c = line.kochC()
            val d = line.kochD()
            val e = line.kochE()

            next.add(KochLine(a, b))
            next.add(KochLine(b, c))
            next.add(KochLine(c, d))
            next.add(KochLine(d, e))
        }
        lines = next

        invalidate()
    }
}