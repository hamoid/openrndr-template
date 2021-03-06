package aBeLibs.extensions

import org.openrndr.Extension
import org.openrndr.Program
import org.openrndr.draw.*

class NoJitter : Extension {
    override var enabled: Boolean = true

    private val a = colorBuffer(16, 16)
    private val b = colorBuffer(16, 16)

    override fun beforeDraw(drawer: Drawer, program: Program) {
        a.copyTo(b)
    }
}
