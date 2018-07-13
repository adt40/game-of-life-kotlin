import java.util.*

class Grid(val width: Int, val height: Int) {
    private val map = hashMapOf<Coordinate, Square>()
    init {
        iterateOverMap{ coordinate ->
            map[coordinate] = Square()
        }
    }

    fun flipAt(coordinate: Coordinate) {
        map[coordinate]?.flip()
    }

    fun isOn(coordinate: Coordinate): Boolean? {
        return map[coordinate]?.isOn()
    }

    fun update(): List<Coordinate> {
        val buffer = initBuffer()
        val changedCoordinates = LinkedList<Coordinate>()
        iterateOverMap { coordinate ->
            val numAdj = getNumAdjacentOn(coordinate)
            val isOn = isOn(coordinate)
            if (isOn != null && isOn) {
                if (numAdj != 2 && numAdj != 3) {
                    buffer.flipAt(coordinate)
                    changedCoordinates.addLast(coordinate)
                }
            } else if (isOn != null && !isOn) {
                if (numAdj == 3) {
                    buffer.flipAt(coordinate)
                    changedCoordinates.addLast(coordinate)
                }
            }
        }
        copyFromBuffer(buffer)
        return changedCoordinates
    }

    fun reset(): List<Coordinate> {
        val changedCoordinates = LinkedList<Coordinate>()
        iterateOverMap { coordinate ->
            val isOn = isOn(coordinate)
            if (isOn != null && isOn) {
                flipAt(coordinate)
                changedCoordinates.addLast(coordinate)
            }
        }
        return changedCoordinates
    }

    private fun copyFromBuffer(buffer: Grid) {
        iterateOverMap { coordinate ->
            val isOn = isOn(coordinate)
            val bufferIsOn = buffer.isOn(coordinate)
            if (isOn != null && bufferIsOn != null && isOn != bufferIsOn) {
                flipAt(coordinate)
            }
        }
    }

    private fun initBuffer(): Grid {
        val buffer = Grid(width, height)
        iterateOverMap { coordinate ->
            val isOn = isOn(coordinate)
            if (isOn != null && isOn) {
                buffer.flipAt(coordinate)
            }
        }
        return buffer
    }

    private fun iterateOverMap(method: (Coordinate) -> Unit) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val coordinate = Coordinate(x, y)
                method(coordinate)
            }
        }
    }

    private fun getNumAdjacentOn(coordinate: Coordinate): Int {
        var totalOn = 0
        for (x in (coordinate.x - 1)..(coordinate.x + 1)) {
            for (y in (coordinate.y - 1)..(coordinate.y + 1)) {
                if (!(x == coordinate.x && y == coordinate.y)) {
                    val adjCoordinate = Coordinate(x, y)
                    val square = map[adjCoordinate]
                    if (square != null && square.isOn()) {
                        totalOn++
                    }
                }
            }
        }
        return totalOn
    }


}