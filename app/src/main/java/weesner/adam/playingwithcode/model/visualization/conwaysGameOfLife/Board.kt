package weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife

import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.*

class Board(val rows: Int, val columns: Int = rows, val defaultCellState: CellState = RANDOM, val staticBoard: ArrayList<Cell>? = null) {
    //var cells: List<List<Cell>> = List(rows) { List(columns) { Cell(defaultCellState) } }
    var cellsNew: ArrayList<Cell> = arrayListOf()

    init {
        //if (staticBoard != null) cells = staticBoard
        if (staticBoard != null) {
            cellsNew = staticBoard
        } else {
            for (x in 0 until rows) {
                for (y in 0 until columns) {
                    cellsNew.add(Cell(defaultCellState, x, y))
                }
            }
        }
    }

    /*fun getNeighborsFor(x: Int, y: Int): List<Cell> {
        val maxRow = rows - 1
        val maxColumn = columns - 1

        val up = x - 1
        val down = x + 1
        val left = y - 1
        val right = y + 1
        val neighbors = arrayListOf<Cell>()

        if (x != 0 && y != 0) neighbors.add(cells[up][left])// top left
        if (x != 0) neighbors.add(cells[up][y])// up
        if (x != 0 && y != maxColumn) neighbors.add(cells[up][right])// top right
        if (y != 0) neighbors.add(cells[x][left])// left
        if (y != maxColumn) neighbors.add(cells[x][right])// right
        if (x != maxRow && y != 0) neighbors.add(cells[down][left])// bottom left
        if (x != maxRow) neighbors.add(cells[down][y])// down
        if (x != maxRow && y != maxColumn) neighbors.add(cells[down][right])// bottom right

        return neighbors
    }*/

    fun getNeighborsFor(cell: Cell): List<Cell> {
        val maxRow = rows - 1
        val maxColumn = columns - 1

        val up = cell.x - 1
        val down = cell.x + 1
        val left = cell.y - 1
        val right = cell.y + 1
        val neighbors = arrayListOf<Cell>()

        if (cell.x != 0 && cell.y != 0) neighbors.add(getCell(up, left))// top left
        if (cell.x != 0) neighbors.add(getCell(up, cell.y))// up
        if (cell.x != 0 && cell.y != maxColumn) neighbors.add(getCell(up, right))// top right
        if (cell.y != 0) neighbors.add(getCell(cell.x, left))// left
        if (cell.y != maxColumn) neighbors.add(getCell(cell.x, right))// right
        if (cell.x != maxRow && cell.y != 0) neighbors.add(getCell(down, left))// bottom left
        if (cell.x != maxRow) neighbors.add(getCell(down, cell.y))// down
        if (cell.x != maxRow && cell.y != maxColumn) neighbors.add(getCell(down, right))// bottom right

        return neighbors
    }

    fun getCell(x: Int, y: Int): Cell {
        return cellsNew.first { it.x == x && it.y == y }
    }

    /*fun aliveNeighbors(x: Int, y: Int): Int {
        return getNeighborsFor(x, y).asSequence().filter { it.state == ALIVE }.count()
    }*/

    fun aliveNeighbors(cell: Cell): Int {
        return getNeighborsFor(cell).asSequence().filter { it.state == ALIVE }.count()
    }

    /*fun cellNextState(x: Int, y: Int): CellState {
        val aliveNeighbors = aliveNeighbors(x, y)
        if (cells[x][y].state == DEAD) {
            return if (aliveNeighbors == 3) ALIVE
            else DEAD
        } else if (cells[x][y].state == ALIVE) {
            return if (aliveNeighbors < 2 || aliveNeighbors > 3) DEAD
            else ALIVE
        }

        return RANDOM
    }*/

    fun cellNextState(cell: Cell): CellState {
        val aliveNeighbors = aliveNeighbors(cell)
        if (cell.state == DEAD) {
            return if (aliveNeighbors == 3) ALIVE
            else DEAD
        } else if (cell.state == ALIVE) {
            return if (aliveNeighbors < 2 || aliveNeighbors > 3) DEAD
            else ALIVE
        }

        return RANDOM
    }

    fun nextState() {
        val newState = arrayListOf<Cell>()
        cellsNew.forEach {
            newState.add(Cell(cellNextState(it), it.x, it.y))
        }
        cellsNew = newState
        /*
        val newState = arrayListOf(arrayListOf<Cell>())
        for (x in 0 until rows) {
            newState.add(arrayListOf())
            for (y in 0 until columns) {
                newState[x].add(y, Cell(cellNextState(x, y)))
            }
        }
        cells = newState
        */
    }


    override fun toString(): String = "Game board that is ${columns}x$rows. Default state is $defaultCellState\n${printBoard()}"
    fun printBoard(): String {
        var cellValues = ""
        for (i in 0 until cellsNew.size) {
            cellValues += "${cellsNew[i]} "
            if ((i + 1) % rows == 0) cellValues += "\n"
        }
        /*cells.forEach { row ->
            row.forEach { cell ->
                cellValues += "${cell.state} "
            }
            cellValues += "\n"
        }*/
        return cellValues
    }
}