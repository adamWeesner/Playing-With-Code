package weesner.adam.playingwithcode.ConwaysGameOfLife

import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.Board
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.Cell
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.ALIVE
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.DEAD
import kotlin.test.Test

class GameBoardTests {
    @Test
    fun `created board with 3 columns and rows`() {
        val gameBoard = Board(3)
        println(gameBoard)
        assert(gameBoard.columns == 3 && gameBoard.rows == 3)
    }

    private fun assertCellsAre(gameBoard: Board, state: CellState) {
        gameBoard.cellsNew.forEach { assert(it.state == state) }

        /*gameBoard.cells.forEach { row ->
            row.forEach { cell ->
                assert(cell.state == state)
            }
        }*/
    }

    @Test
    fun `created board with all alive`() {
        val gameBoard = Board(3, defaultCellState = ALIVE)
        println(gameBoard)
        assertCellsAre(gameBoard, ALIVE)
    }

    @Test
    fun `created board with all dead`() {
        val gameBoard = Board(3, defaultCellState = DEAD)
        println(gameBoard)
        assertCellsAre(gameBoard, DEAD)
    }

    private fun assertCellNeighborSize(gameBoard: Board, cellX: Int, cellY: Int, neighborsCount: Int) {
        val cellWithNeighbors = gameBoard.getCell(cellX, cellY)
        val neighbors = gameBoard.getNeighborsFor(cellWithNeighbors)
        println(gameBoard)
        println("neighbors for $cellWithNeighbors:")
        for (i in 0 until neighbors.size) println(neighbors[i])
        assert(neighbors.size == neighborsCount)
        /*
        val gameBoard = Board(5)
        val neighbors = gameBoard.getNeighborsFor(cellX, cellY)
        println(gameBoard)
        println("neighbors for ${gameBoard.cells[cellX][cellY]}:")
        for (i in 0 until neighbors.size) println(neighbors[i])
        assert(neighbors.size == neighborsCount)
        */
    }

    @Test
    fun `center neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, 2, 2, 8)
    }

    @Test
    fun `top left neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, 0, 0, 3)
    }

    @Test
    fun `bottom left neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, gameBoard.rows - 1, 0, 3)
    }

    @Test
    fun `bottom right neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, gameBoard.rows - 1, gameBoard.columns - 1, 3)
    }

    @Test
    fun `top right neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, 0, gameBoard.columns - 1, 3)
    }

    @Test
    fun `center bottom neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, gameBoard.rows - 1, 2, 5)
    }

    @Test
    fun `center top neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, 0, 2, 5)
    }

    @Test
    fun `center left neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, 2, 0, 5)
    }

    @Test
    fun `center right neighbors of 5x5`() {
        val gameBoard = Board(5)
        assertCellNeighborSize(gameBoard, 2, gameBoard.columns - 1, 5)
    }

    @Test
    fun `cell has 3 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(DEAD, 0, 1), Cell(ALIVE, 0, 2),
                Cell(DEAD, 1, 0), Cell(DEAD, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)
        val alive = gameBoard.aliveNeighbors(gameBoard.getCell(1, 1))
        assert(alive == 3)
    }

    @Test
    fun `living cell should die with 0 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(DEAD, 0, 1), Cell(ALIVE, 0, 2),
                Cell(DEAD, 1, 0), Cell(DEAD, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(0, 0))

        assert(cellsNextState == DEAD)
    }

    @Test
    fun `living cell should die with 1 alive neighbor`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(DEAD, 1, 0), Cell(DEAD, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(0, 0))

        assert(cellsNextState == DEAD)
    }

    @Test
    fun `living cell should stay alive with 2 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(ALIVE, 1, 0), Cell(ALIVE, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(0, 0))

        assert(cellsNextState == ALIVE)
    }

    @Test
    fun `living cell should stay alive with 3 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(ALIVE, 1, 0), Cell(ALIVE, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(0, 0))

        assert(cellsNextState == ALIVE)
    }

    @Test
    fun `living cell should die with 4 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(ALIVE, 1, 0), Cell(ALIVE, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(1, 0))

        assert(cellsNextState == DEAD)
    }

    @Test
    fun `dead cell should come alive with exactly 3 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(ALIVE, 1, 0), Cell(ALIVE, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(2, 1))

        assert(gameBoard.getCell(2, 1).state == DEAD && cellsNextState == ALIVE)
    }

    @Test
    fun `dead cell should stay dead with 2 alive neighbors`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(ALIVE, 1, 0), Cell(DEAD, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)

        val cellsNextState = gameBoard.cellNextState(gameBoard.getCell(2, 1))

        assert(gameBoard.getCell(2, 1).state == DEAD && cellsNextState == DEAD)
    }

    @Test
    fun `game board state should update to next state correctly`() {
        val board = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(ALIVE, 0, 2),
                Cell(ALIVE, 1, 0), Cell(DEAD, 1, 1), Cell(DEAD, 1, 2),
                Cell(ALIVE, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val nextState = arrayListOf(
                Cell(ALIVE, 0, 0), Cell(ALIVE, 0, 1), Cell(DEAD, 0, 2),
                Cell(ALIVE, 1, 0), Cell(DEAD, 1, 1), Cell(DEAD, 1, 2),
                Cell(DEAD, 2, 0), Cell(DEAD, 2, 1), Cell(DEAD, 2, 2)
        )

        val gameBoard = Board(3, staticBoard = board)
        println(gameBoard)
        gameBoard.nextState()
        println(gameBoard)

        var nextCounter = 0
        for (x in 0 until gameBoard.rows) {
            for (y in 0 until gameBoard.columns) {
                assert(gameBoard.getCell(x, y).state == nextState[nextCounter].state)
                nextCounter++
            }
        }
    }
}