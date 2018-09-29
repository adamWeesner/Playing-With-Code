package weesner.adam.playingwithcode.ConwaysGameOfLife

import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.Cell
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.*
import kotlin.test.Test

class CellTests {
    @Test
    fun `has state of alive`() {
        val cell = Cell(ALIVE)
        println(cell)
        assert(cell.state == ALIVE)
    }

    @Test
    fun `has state of dead`() {
        val cell = Cell(DEAD)
        println(cell)
        assert(cell.state == DEAD)
    }

    @Test
    fun `created with random state of 0 or 1`() {
        val cell = Cell.createWithRandomState()
        println(cell)
        assert(cell.state == ALIVE || cell.state == DEAD)
    }

    @Test
    fun `created with CellState RANDOM and reassigned`() {
        val cell = Cell(RANDOM)
        println(cell)
        assert(cell.state == ALIVE || cell.state == DEAD)
    }
}