package weesner.adam.playingwithcode.ConwaysGameOfLife

import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState
import weesner.adam.playingwithcode.model.visualization.conwaysGameOfLife.CellState.*
import kotlin.test.Test

class CellStateTests {
    @Test
    fun `has dead state`() {
        assert(enumValues<CellState>().contains(DEAD))
    }

    @Test
    fun `has alive state`() {
        assert(enumValues<CellState>().contains(ALIVE))
    }

    @Test
    fun `has random state`() {
        assert(enumValues<CellState>().contains(RANDOM))
    }
}