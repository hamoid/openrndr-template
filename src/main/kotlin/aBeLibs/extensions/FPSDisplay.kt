package aBeLibs.extensions

import org.openrndr.Extension
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.isolated
import org.openrndr.math.Matrix44

class FPSDisplay() : Extension {
    override var enabled: Boolean = true

    private var frames = 0
    private var lastSecond = 0
    private var fps = 60

    override fun setup(program: Program) {
        lastSecond = program.seconds.toInt()
    }

    override fun afterDraw(drawer: Drawer, program: Program) {
        val now = program.seconds.toInt()
        if(lastSecond != now) {
            lastSecond = now
            fps = frames
            frames = 0
        }
        frames++

        drawer.isolated {
            drawer.view = Matrix44.IDENTITY
            drawer.fill = ColorRGBa.BLACK
            drawer.ortho()
            drawer.text(fps.toString(), drawer.width - 100.0,  40.0)
        }
    }
}