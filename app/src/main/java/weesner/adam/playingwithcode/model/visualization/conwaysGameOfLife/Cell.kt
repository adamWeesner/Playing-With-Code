package weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife

import java.util.*

class Cell(var state: CellState, var x: Int = -1, var y: Int = -1) {
    init {
        if (state == CellState.RANDOM) state = assignRandomState()
    }

    override fun toString(): String = "Cell with state of $state | $x $y"

    companion object {
        fun createWithRandomState(): Cell {
            val randomState = Random().nextInt(2)

            return if (randomState == 0) Cell(CellState.DEAD)
            else Cell(CellState.ALIVE)
        }

        fun assignRandomState(): CellState {
            val randomState = Random().nextInt(2)

            return if (randomState == 0) CellState.DEAD
            else CellState.ALIVE
        }
    }
}