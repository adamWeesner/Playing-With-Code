package weesner.adam.playingwithcode.model.visualization

import android.graphics.Canvas
import android.graphics.Paint
import weesner.adam.playingwithcode.utils.radians
import weesner.adam.playingwithcode.utils.rotate
import weesner.adam.playingwithcode.utils.visualization.Point

class KochLine(val start: Point, val end: Point) {
    fun kochA(): Point = Point(start.x, start.y)

    // third of the way
    fun kochB(): Point {
        val a = end.subtract(start).divide(3).add(start)
        return Point(a.x, a.y)
    }

    fun kochC(): Point {
        val b = end.subtract(start).divide(3)
        var a = start.add(b)

        rotate(b, -radians(60))
        a = a.add(b)

        return Point(a.x, a.y)
    }

    fun kochD(): Point {
        val a = end.subtract(start).multiply(2 / 3f).add(start)
        return Point(a.x, a.y)
    }

    fun kochE(): Point = Point(end.x, end.y)

    fun display(canvas: Canvas?, paint: Paint) {
        canvas!!.drawLine(start.x, start.y, end.x, end.y, paint)
    }

    override fun toString(): String {
        return "$start: $end"
    }
}