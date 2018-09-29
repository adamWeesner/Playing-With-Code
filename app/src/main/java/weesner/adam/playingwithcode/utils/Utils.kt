package weesner.adam.playingwithcode.utils

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import weesner.adam.playingwithcode.utils.visualization.Point
import java.text.NumberFormat
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun radians(angle: Int): Float {
    val radian = angle * PI / 180
    return radian.toFloat()
}

fun rotate(point: Point, angle: Float) {
    val tmp = point.x
    point.x = (point.x * cos(angle)) - (point.y * sin(angle))
    point.y = (tmp * sin(angle)) + (point.y * cos(angle))
}

fun Int.toDP(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(), Resources.getSystem().displayMetrics)
}

fun Float.toDP(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this, Resources.getSystem().displayMetrics)
}

fun map(value: Float, iStart: Float, iStop: Float, eStart: Float, eStop: Float): Float {
    return eStart + (eStop - eStart) * ((value - iStart) / (iStop - iStart))
}

var oldTime = System.currentTimeMillis() - 1000

fun calcFPS(): String? {
    val newTime = System.currentTimeMillis()
    val delta = newTime - oldTime
    val fps: Double = (1 / (delta / 1000.toDouble()))
    oldTime = newTime
    val formatter = NumberFormat.getInstance()
    return formatter.format(fps)
}

fun fps(canvas: Canvas, paint: Paint) {
    val fps = calcFPS()

    paint.color = Color.BLACK
    paint.textSize = 20.toDP()
    canvas.drawText("FPS: $fps", 32f, 64f, paint)
}

val deviceHeight: Int = Resources.getSystem().displayMetrics.heightPixels
val deviceWidth: Int = Resources.getSystem().displayMetrics.widthPixels
val View.centerX: Float get() = (width * .5).toFloat()
val View.centerY: Float get() = (height * .5).toFloat()