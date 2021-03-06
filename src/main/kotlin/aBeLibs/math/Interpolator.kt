package aBeLibs.math

import org.openrndr.math.clamp
import org.openrndr.math.map
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

class Interpolator(
    private var currentValue: Double = 0.0,
    private var maxAcceleration: Double = 0.000025, // 0.008
    private var maxSpeed: Double = 0.002, // 0.0001
    private var dampDist: Double = 0.25 // 0.25
) {
    var targetValue: Double = 0.0
    private var currentSpeed = 0.0

    fun getNext(): Double {
        var offset = targetValue - currentValue
        val d = min(dampDist, abs(offset))
        offset = offset.sign *
                map(0.0, dampDist, 0.0, maxSpeed, d)
        val acceleration = clamp(
            offset - currentSpeed,
            -maxAcceleration, maxAcceleration
        )

        currentSpeed += acceleration
        currentSpeed = clamp(currentSpeed, -maxSpeed, maxSpeed)

        currentValue += currentSpeed

        return currentValue
    }

    @Suppress("unused")
    fun jumpTo(target: Double) {
        currentSpeed = 0.0
        currentValue = target
        targetValue = target
    }

    @Suppress("unused")
    fun setSpeeds(maxSpeed: Double, maxAcceleration: Double, dampDistance: Double) {
        this.maxSpeed = maxSpeed
        this.maxAcceleration = maxAcceleration
        this.dampDist = dampDistance
    }

    operator fun times(v: Number) = currentValue * v.toDouble()
    fun toDouble() = currentValue
}