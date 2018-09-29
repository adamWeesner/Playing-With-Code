package weesner.adam.playingwithcode.ui.visualizations

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.Board
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.ALIVE
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.DEAD
import weesner.adam.playingwithcode.utils.calcFPS
import weesner.adam.playingwithcode.utils.toDP

class ConwaysGameOfLife {
    lateinit var linearLayout: LinearLayout
    lateinit var gameBoard: Board
    lateinit var startButton: Button
    lateinit var endButton: Button
    lateinit var sizeEditText: EditText
    lateinit var boardLayout: LinearLayout
    lateinit var fpsTextView: TextView

    lateinit var main: ConstraintLayout

    var anyAlive = true
    var fps = ""

    fun create(mainView: ConstraintLayout) {
        main = mainView
        setupViews()
        setupListeners()
    }

    fun setupViews() {
        linearLayout = LinearLayout(main.context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)

        sizeEditText = EditText(main.context)
        sizeEditText.hint = "Cells per row and column (always square)"
        sizeEditText.inputType = InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_SIGNED
        sizeEditText.setTextColor(Color.BLACK)
        sizeEditText.setText("30")

        val buttonParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        buttonParams.marginStart = Math.round(36.toDP())
        buttonParams.marginEnd = Math.round(36.toDP())
        buttonParams.topMargin = Math.round(16.toDP())
        buttonParams.bottomMargin = Math.round(16.toDP())

        startButton = Button(main.context)
        startButton.text = "Start"
        startButton.setTextColor(Color.BLACK)
        startButton.layoutParams = buttonParams

        endButton = Button(main.context)
        endButton.text = "Stop"
        endButton.setTextColor(Color.BLACK)
        endButton.layoutParams = buttonParams
        endButton.visibility = View.INVISIBLE

        boardLayout = LinearLayout(main.context)
        boardLayout.orientation = LinearLayout.VERTICAL
        val smallest = if (main.width > main.height) main.height else main.width
        boardLayout.layoutParams = LinearLayout.LayoutParams(smallest, smallest)

        fpsTextView = TextView(main.context)
        fpsTextView.layoutParams = buttonParams
        fpsTextView.text = fps

        linearLayout.addView(sizeEditText)
        linearLayout.addView(startButton)
        linearLayout.addView(boardLayout)
        linearLayout.addView(endButton)
        linearLayout.addView(fpsTextView)
        main.addView(linearLayout)
    }

    fun setupListeners() {
        startButton.setOnClickListener {
            clearBoard()
            if (sizeEditText.text.isNullOrBlank()) {
                Toast.makeText(linearLayout.context, "You need to input a value for size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            gameBoard = Board(sizeEditText.text.toString().toInt())
            createBoard(main.context, gameBoard)
            startButton.visibility = View.INVISIBLE
            endButton.visibility = View.VISIBLE
            anyAlive = true
            runGame(gameBoard)
        }

        endButton.setOnClickListener {
            anyAlive = false
            resetEverything()
        }
    }

    fun createBoard(context: Context, gameBoard: Board) {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1f
        for (y in 0 until gameBoard.columns) {
            val row = LinearLayout(context)
            row.orientation = LinearLayout.HORIZONTAL
            row.layoutParams = params
            row.tag = "column"

            for (x in 0 until gameBoard.rows) {
                val cell = View(context)
                cell.tag = "$y $x"
                cell.layoutParams = params
                cell.setBackgroundColor(Color.WHITE)
                row.addView(cell)
            }
            boardLayout.addView(row)
        }
    }

    class ChangeCell(val change: Boolean, val cell: View, val color: Int)

    fun setBoardState(gameBoard: Board) {
        val changeCell = arrayListOf<ChangeCell>()
        launch(UI) {
            withContext(DefaultDispatcher) {
                gameBoard.cellsNew.forEach { cell ->
                    val view = boardLayout.findViewWithTag<View>("${cell.y} ${cell.x}")
                    var change = false
                    var color = Color.BLACK
                    if (view != null) {
                        color = (view.background as ColorDrawable).color
                        if (color == Color.WHITE) { // was dead
                            if (cell.state == ALIVE) change = true// now alive
                        } else if (color == Color.BLACK) {// was alive
                            if (cell.state == DEAD) change = true// now dead
                        }
                    }
                    changeCell.add(ChangeCell(change, view, if (color == Color.WHITE) Color.BLACK else Color.WHITE))
                }
            }
            changeCell.forEach {
                if (it.change) it.cell.setBackgroundColor(it.color)
            }
        }
    }

    fun runGame(gameBoard: Board) {
        launch(UI) {
            withContext(DefaultDispatcher) {
                fps = "FPS: ${calcFPS()!!}"
                delay(100)
                gameBoard.nextState()
                if (anyAlive) anyAlive = gameBoard.cellsNew.asSequence().filter { it.state == ALIVE }.count() != 0
            }
            if (anyAlive) {
                fpsTextView.text = fps
                setBoardState(gameBoard)
                runGame(gameBoard)
            } else {
                Toast.makeText(linearLayout.context, "All cells have died", Toast.LENGTH_SHORT).show()
                resetEverything()
            }
        }
    }

    fun resetEverything() {
        endButton.visibility = View.INVISIBLE
        startButton.visibility = View.VISIBLE
        clearBoard()
        fps = ""
        fpsTextView.text = fps
    }

    fun clearBoard() {
        anyAlive = false
        boardLayout.removeAllViews()
    }
}