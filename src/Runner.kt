import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {

    val width  = 60
    val height = 60
    val gridSize = 15

    val grid = Grid(width, height)

    val panel = GOLPanel(grid, width, height, gridSize)
    val frame = JFrame()
    frame.contentPane.add(panel)
    frame.setSize(width * gridSize, height * gridSize)
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.isVisible = true

}