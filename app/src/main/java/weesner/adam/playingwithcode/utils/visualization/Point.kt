package weesner.adam.playingwithcode.utils.visualization

import android.graphics.Canvas
import android.graphics.Paint

open class Point(var x: Float, var y: Float) {
    fun multiply(n: Float): Point = Point(x * n, y * n)

    fun divide(n: Int): Point = Point(x / n, y / n)

    fun add(point: Point): Point = Point(x + point.x, y + point.y)

    fun subtract(point2: Point): Point = Point(x - point2.x, y - point2.y)

    override fun toString(): String = "$x, $y"

    fun display(radius: Float = 1f, canvas: Canvas?, paint: Paint) {
        if (x >= 0 && x < canvas!!.width && y >= 0 && y < canvas.height)
            canvas.drawCircle(x, y, radius, paint)
    }
}