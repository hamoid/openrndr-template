package intersections

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.shape.contour
import org.openrndr.shape.intersections

/**
 * Test what happens when finding intersections between
 * two identical contours. How many points do we get?
 *
 * This used to give one intersection too few until this PR:
 * https://github.com/openrndr/openrndr/pull/201
 */

fun main() = application {

    program {
        val b : (Double, Double) -> Vector2 = drawer.bounds::position
        val c = contour {
            moveTo(b(0.2, 0.2))
            curveTo(b(0.5, 0.65), b(0.8, 0.2))
            lineTo(b(0.20, 0.80))
            curveTo(b(0.5, 0.35), b(0.8, 0.8))
            close()
        }
        val ints = intersections(c, c).map {
            it.position
        }
        println("--")
        extend {
            drawer.contour(c)

            drawer.fill = ColorRGBa.GREEN
            drawer.circles(ints, 10.0)
        }
    }
}
