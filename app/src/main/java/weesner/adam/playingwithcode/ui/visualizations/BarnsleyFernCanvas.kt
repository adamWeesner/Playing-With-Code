package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import weesner.adam.playingwithcode.utils.fps
import weesner.adam.playingwithcode.utils.map
import weesner.adam.playingwithcode.utils.visualization.Point
import java.util.*

class BarnsleyFernCanvas(context: Context) : View(context) {
    var paint = Paint()
    var nextX: Float = 0f
    var nextY: Float = 0f

    var points = arrayListOf<Point>()
    val random = Random(System.currentTimeMillis())
    var keepGoing = true

    var bitmap: Bitmap? = null
    var bitmapCanvas: Canvas? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL_AND_STROKE

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmapCanvas = Canvas(bitmap)
        }

        for (p: Point in points) {
            val pixel = bitmap!!.getPixel(p.x.toInt(), p.y.toInt())
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)

            if (red == 0 && green == 0 && blue == 0) {
                paint.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
                p.display(2f, bitmapCanvas, paint)
            }
        }

        canvas!!.drawBitmap(bitmap, 0f, 0f, paint)
        fps(canvas, paint)

        points.clear()
        for (i in 0 until 250) {
            if (keepGoing) {
                calculateNext()
                invalidate()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        keepGoing = !keepGoing
        if (keepGoing) invalidate()
        return super.onTouchEvent(event)
    }

    fun calculateNext() {
        var point: Point? = Point(0f, 0f)
        launch(UI) {
            withContext(DefaultDispatcher) {
                val px = map(x, -2.1820f, 2.6558f, 0f, width.toFloat())
                val py = map(y, 0f, 9.9983f, height.toFloat(), 0f)

                if (px > 0 && px < bitmap!!.width && py > 0 && py < bitmap!!.height) {
                    point!!.x = px
                    point!!.y = py
                } else {
                    point = null
                }

                val next = random.nextFloat()

                when {
                    next < .01 -> {
                        nextX = 0f
                        nextY = .16f * y
                    }
                    next < .86 -> {
                        nextX = ((.85 * x) + (.04 * y)).toFloat()
                        nextY = ((-.04 * x) + (.85 * y) + 1.6).toFloat()
                    }
                    next < .93 -> {
                        nextX = ((.20 * x) + (-.26 * y)).toFloat()
                        nextY = ((.23 * x) + (.22 * y) + 1.6).toFloat()
                    }
                    else -> {
                        nextX = ((-.15 * x) + (.28 * y)).toFloat()
                        nextY = ((.26 * x) + (.24 * y) + .44).toFloat()
                    }
                }

                x = nextX
                y = nextY
            }
            if (point != null) points.add(point!!)
        }

    }
}