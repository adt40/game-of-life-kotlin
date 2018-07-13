import java.awt.Color
import java.awt.Graphics
import java.awt.event.*
import java.util.*
import java.util.Timer
import javax.swing.*

class GOLPanel(val grid: Grid, val gridWidth: Int, val gridHeight: Int, val gridSize: Int): JPanel(), MouseListener {

    private val frameIncrement = 25
    private var timeBetweenFrames: Long = 200
    private var timer = Timer()
    private var running = false

    init {
        addMouseListener(this)
        setKeyBindings()
    }

    override fun paint(g: Graphics) {
        for (x in 0 until gridWidth) {
            for (y in 0 until gridHeight) {
                val coordinate = Coordinate(x, y)
                val isOn = grid.isOn(coordinate)
                if (isOn != null && isOn) {
                    g.color = Color.GREEN
                } else {
                    g.color = Color.WHITE
                }
                g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize)
                g.color = Color.BLACK
                g.drawRect(x * gridSize, y * gridSize, gridSize, gridSize)
            }
        }
    }

    private fun repaintChanged(changedCoordinates: List<Coordinate>) {
        for (coordinate in changedCoordinates) {
            val x = coordinate.x * gridSize
            val y = coordinate.y * gridSize
            repaint(x, y, gridSize, gridSize)
        }
    }

    private fun run() {
        class Task(): TimerTask() {
            override fun run() {
                repaintChanged(grid.update())
            }
        }
        timer = Timer()
        timer.schedule(Task(), timeBetweenFrames, timeBetweenFrames)
    }


    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
    override fun mousePressed(e: MouseEvent?) {
        if (e != null) {
            val x = e.x / gridSize
            val y = e.y / gridSize
            val coordinate = Coordinate(x, y)
            grid.flipAt(coordinate)
            repaintChanged(listOf(coordinate))
        }
    }

    private fun setKeyBindings() {
        val actionMap = actionMap
        val inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)

        val space = " "
        val f = "f"
        val s = "s"
        val d = "d"
        val a = "a"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), space)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), f)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), s)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), d)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), a)

        actionMap.put(space, KeyAction(space))
        actionMap.put(f, KeyAction(f))
        actionMap.put(s, KeyAction(s))
        actionMap.put(d, KeyAction(d))
        actionMap.put(a, KeyAction(a))
    }

    private inner class KeyAction internal constructor(actionCommand: String) : AbstractAction() {
        init {
            putValue(Action.ACTION_COMMAND_KEY, actionCommand)
        }

        override fun actionPerformed(actionEvt: ActionEvent) {
            if (actionEvt.actionCommand != null) {
                if (actionEvt.actionCommand == " ") {
                    if (running) {
                        timer.cancel()
                    } else {
                        run()
                    }
                    running = !running
                }
                else if (actionEvt.actionCommand == "f") {
                    repaintChanged(grid.update())
                }
                else if (actionEvt.actionCommand == "s") {
                    if (timeBetweenFrames > frameIncrement) {
                        timeBetweenFrames -= frameIncrement
                        if (running) {
                            timer.cancel()
                            run()
                        }
                    }
                }
                else if (actionEvt.actionCommand == "a") {
                    timeBetweenFrames += frameIncrement
                    if (running) {
                        timer.cancel()
                        run()
                    }
                }
                else if (actionEvt.actionCommand == "d") {
                    repaintChanged(grid.reset())
                    timer.cancel()
                    running = false
                }
            }
        }
    }
}