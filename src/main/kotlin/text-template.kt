import org.openrndr.KEY_ESCAPE
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineJoin
import org.openrndr.draw.isolated
import org.openrndr.extensions.Screenshots
import org.openrndr.math.transforms.transform
import org.openrndr.shape.Rectangle
import org.openrndr.shape.ShapeContour
import org.openrndr.shape.rectangleBounds
import org.openrndr.svg.loadSVG
import kotlin.system.exitProcess

/**
 * Writing text
 * - Inspiration: https://duckduckgo.com/?t=ffab&q=patent+drawing&iax=images&ia=images
 * - Inspiration: https://stackoverflow.com/questions/27893042/text-to-phonemes-converter
 *
 * - in inkscape, have labeled rectangles and draw on top of them.
 *   then openrndr scans the file, makes a list of rectangles, looks at their IDs,
 *   finds shapes inside them, stores them on a dictionary, then I can write
 *   using that hand writing without creating a font. Also, the shapes are made out
 *   of lines, better for the axidraw
 *
 */

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        var svg = loadSVG("data/text-template-3.svg")

        val bounds = mutableMapOf<Char, Rectangle>()
        val letters = mutableMapOf<Char, MutableList<ShapeContour>>()

        svg.findShapes().filter { it.id!!.length <= 4 }.forEach {
            val id = it.id!!
            val idChar = if (id.length == 1) id.last() else id.substring(1).toInt().toChar()
            bounds[idChar] = it.bounds
        }
        svg.findShapes().filter { it.id!!.length > 4 }.forEach { curve ->
            for (b in bounds) {
                if (b.value.contains(curve.shape.contours.first().segments.first().start)) {
                    if (!letters.containsKey(b.key)) {
                        letters[b.key] = mutableListOf()
                    }
                    letters[b.key]!!.add(curve.shape.outline.transform(transform {
                        translate(-b.value.corner)
                    }))
                    break
                }
            }
        }

        val text = listOf(
            "I'm baby kogi poke DIY shaman cold-pressed. Palo santo pitchfork trust",
            "fund wayfarers. Humblebrag direct trade pour-over, fanny pack venmo vinyl",
            "succulents roof party gastropub portland mustache thundercats. Fingerstache",
            "tumblr dreamcatcher coloring book, brunch bicycle rights bitters health goth",
            "chia snackwave cloud bread leggings ennui heirloom pickled.",
            "",
            "Knausgaard echo park twee tacos, helvetica coloring book enamel pin man",
            "braid lomo photo booth cronut ennui hot chicken. DIY selfies disrupt",
            "gochujang squid cold-pressed. Yuccie microdosing freegan sartorial.",
            "Microdosing narwhal fanny pack dreamcatcher ramps godard."
        )

        extend(Screenshots())
        extend {
            drawer.background(ColorRGBa.WHITE)
            drawer.fill = null
            drawer.stroke = ColorRGBa.BLACK
            drawer.lineJoin = LineJoin.BEVEL
            drawer.translate(100.0, 100.0)
            text.forEach { line ->
                drawer.isolated {
                    line.trimIndent().toUpperCase().forEach {
                        letters[it]?.run {
                            val letterBounds = rectangleBounds(this.map { c -> c.bounds })
                            drawer.translate(-letterBounds.x, 0.0)
                            drawer.contours(this)
                            drawer.translate(letterBounds.x + letterBounds.width + 3.0, 0.0)
                        } ?: drawer.translate(10.0, 0.0)
                    }
                }
                drawer.translate(0.0, 20.0)
            }
        }

        keyboard.keyDown.listen {
            when (it.key) {
                KEY_ESCAPE -> exitProcess(0)
            }
        }
    }
}
